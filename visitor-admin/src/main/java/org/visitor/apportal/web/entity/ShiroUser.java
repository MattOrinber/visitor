package org.visitor.apportal.web.entity;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * Shiro框架的用戶信息, 自定义Authentication对象, 使得Subject携带用户的登录名和菜单信息.
 * 
 * @author yong.cao
 * @create-time 2013-10-17
 * @revision 1.0.0
 * @E_mail yong.cao@renren-inc.com
 */
public class ShiroUser implements Serializable {
	private static final long serialVersionUID = -1373760761780840081L;

	private long id;

	private String loginName;

	private String first_name;

	private String last_name;

	public ShiroUser(long id, String loginName, String first_name,
			String last_name) {
		this.id = id;
		this.loginName = loginName;
		this.first_name = first_name;
		this.last_name = last_name;
	}

	public long getId() {
		return id;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	/**
	 * 本函数输出将作为默认的<shiro:principal/>输出.
	 */
	@Override
	public String toString() {
		return first_name + " " + last_name;
	}

	/**
	 * 重载hashCode,只计算loginName;
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(loginName);
	}

	/**
	 * 重载equals,只计算loginName;
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ShiroUser other = (ShiroUser) obj;
		if (loginName == null) {
			if (other.loginName != null) {
				return false;
			}
		} else if (!loginName.equals(other.loginName)) {
			return false;
		}
		return true;
	}
}
