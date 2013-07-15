package hudson.plugins.concurrent_login;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jenkins.model.Jenkins;

import hudson.Extension;
import hudson.model.PageDecorator;
import hudson.model.Hudson;

import org.acegisecurity.Authentication;
import org.kohsuke.stapler.export.Exported;

import hudson.plugins.concurrent_login.UserInfoProperty.DescriptorImpl;

@Extension
public class UserInfoDecorator extends PageDecorator {

	private String sess_id;
	private String alreadyLoginID;

	final Logger logger = Logger.getLogger("hudson.plugins.concurrent_login");

	public UserInfoDecorator() {
		logger.log(Level.INFO, "***** Loading UserInfoDecorator...");
		load();
	}

	/**
	 * Used by <tt>footer.jelly</tt> to control session behavior.
	 * 
	 * @param HttpServletRequest
	 *            request
	 * @param HttpServletResponse
	 *            response
	 * @param String
	 *            loginUserID
	 * 
	 */
	public void requestInfo(HttpServletRequest request,
			HttpServletResponse response, String loginUserID) {
		
		DescriptorImpl descriptor = Hudson.getInstance().getDescriptorByType(UserInfoProperty.DescriptorImpl.class);
		
		if (descriptor.getuseConcurrentLogin()) {
			alreadyLoginID = null;

			HttpSession session = request.getSession(false);
			sess_id = session.getId();

			SessionManager sessionManager = SessionManager.getInstance();

			if (!loginUserID.equals("anonymous")) {
				// logger.log(Level.INFO, "******** sess_id ... " + sess_id +
				// " / isLogin ... " + sessionManager.isLogin(sess_id));
				if (!sessionManager.isLogin(sess_id)) {
					// Note: Session Check: Jenkins(O), Custom(X)
					if (!sessionManager.isUsing(loginUserID)) {
						sessionManager.setSession(session, loginUserID);
						// logger.log(Level.INFO, "******** setSession ... " +
						// loginUserID + " / getID ... " +
						// sessionManager.getID());
					} else {
						alreadyLoginID = loginUserID;
						// logger.log(Level.INFO,
						// "******** Already using ID ... " + loginUserID);
						try {
							response.sendRedirect("/jenkins/logout");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public String getAlreadyLoginCheck() {
		return alreadyLoginID;
	}

	public String getSessId() {
		return sess_id;
	}

	@Exported
	public String getName() {
		return auth().getName();
	}

	private Authentication auth() {
		return Jenkins.getAuthentication();
	}

    //public boolean getuseInterceptConcurrentLogin() {
    //    return Hudson.getInstance().getDescriptorByType(UserInfoProperty.DescriptorImpl.class).getuseConcurrentLogin();
    //}
}
