package tokenmanagement.extension;

import javax.enterprise.context.ApplicationScoped;

import tokenmanagement.common.IMyCDIBean;


@ApplicationScoped
public class MyCDIBean implements IMyCDIBean {
	
	public String ping() {
        return MyCDIBean.class.getSimpleName();
    }

}
