package hudson.plugins.concurrent_login;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class UserInfoProperty extends AbstractDescribableImpl<UserInfoProperty> {

    @DataBoundConstructor
    public UserInfoProperty() {
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<UserInfoProperty> {
    	/**
    	 * To persist global configuration information, simply store it in a field
    	 * and call save().
    	 * 
    	 * <p>
    	 * If you don't want fields to be persisted, use <tt>transient</tt>.
    	 */
    	private boolean useConcurrentLogin = false;

        public DescriptorImpl() {
            load();
        }

        @Override
        public String getDisplayName() {
            return null;
        }
        
    	@Override
    	public boolean configure(StaplerRequest req, JSONObject formData) {
            useConcurrentLogin = true;
    		// useConcurrentLogin = formData
    		//		.getBoolean("useInterceptConcurrentLogin");
    		
    		save();
    		
    		return true;
    	}

    	public boolean getuseConcurrentLogin() {
    		return useConcurrentLogin;
    	}
    }

}