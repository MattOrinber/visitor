package org.visitor.appportal.web.mailutils;

import org.visitor.appportal.web.utils.MixAndMatchUtils;

public class SendMailUtils
{
	private String toAddress;
	private String title;
	private String content;

	private static final String MAIL_SERVER_HOST = MixAndMatchUtils.getSystemConfig("mail_server_host");
	private static final String MAIL_SERVER_PORT = MixAndMatchUtils.getSystemConfig("mail_server_port");
	private static final String MAIL_SERVER_USERNAME = MixAndMatchUtils.getSystemConfig("mail_server_username");
	private static final String MAIL_SERVER_PASSWORD = MixAndMatchUtils.getSystemConfig("mail_server_password");
	public static final String MAIL_SEND_USERNAME_ALIAS = MixAndMatchUtils.getSystemConfig("mail_server_username_alias");

	public void sendEmail()
	{
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(MAIL_SERVER_HOST);
		mailInfo.setMailServerPort(MAIL_SERVER_PORT);
		mailInfo.setValidate(true);
		mailInfo.setUserName(MAIL_SERVER_USERNAME);
		mailInfo.setPassword(MAIL_SERVER_PASSWORD);// 您的邮箱密码
		mailInfo.setFromAddress(MAIL_SERVER_USERNAME);

		mailInfo.setToAddress(toAddress);
		mailInfo.setSubject(title);
		mailInfo.setContent(content);

		SimpleMailSender sms = new SimpleMailSender();
		sms.sendTextMail(mailInfo);// 发送文体格式
	}

	public void sendEmailHtml() throws UserMailException
	{
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(MAIL_SERVER_HOST);
		mailInfo.setMailServerPort(MAIL_SERVER_PORT);
		mailInfo.setValidate(true);
		mailInfo.setUserName(MAIL_SERVER_USERNAME);
		mailInfo.setPassword(MAIL_SERVER_PASSWORD);// 您的邮箱密码
		mailInfo.setFromAddress(MAIL_SERVER_USERNAME);

		mailInfo.setToAddress(toAddress);
		mailInfo.setSubject(title);
		mailInfo.setContent(content);

		SimpleMailSender sms = new SimpleMailSender();
		sms.sendHtmlMail(mailInfo);// 发送html格式
	}

	public String getToAddress()
	{
		return toAddress;
	}

	public void setToAddress(String toAddress)
	{
		this.toAddress = toAddress;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}
