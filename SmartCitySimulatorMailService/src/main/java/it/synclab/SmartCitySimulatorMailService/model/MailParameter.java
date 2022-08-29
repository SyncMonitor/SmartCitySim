package it.synclab.SmartCitySimulatorMailService.model;

import java.util.Date;

public class MailParameter {

	private String from;
	private String replyTo;
	private String[] to;
	private String[] cc;
	private String[] bcc;
	private Date sentDate;
	private String subject;
	private String text;

    public MailParameter() {
    }

    public MailParameter(String from, String replyTo, String[] to, String[] cc, String[] bcc, Date sentDate, String subject, String text) {
        this.from = from;
        this.replyTo = replyTo;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.sentDate = sentDate;
        this.subject = subject;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "MailParameter{" +
                "from='" + from + '\'' +
                ", replyTo='" + replyTo + '\'' +
                ", to=" + to +
                ", cc=" + cc +
                ", bcc=" + bcc +
                ", sentDate=" + sentDate +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                '}';
    } 
}
