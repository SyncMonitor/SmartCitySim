package it.synclab.smartparking.model;

public class Maintainer {

	private String name;
	private String surname;
	private String company;
	private String phoneNumber;
	private String mail;

	public Maintainer() {
	}

	public Maintainer(String name, String surname, String company, String phoneNumber, String mail) {
		this.name = name;
		this.surname = surname;
		this.company = company;
		this.phoneNumber = phoneNumber;
		this.mail = mail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@Override
	public String toString() {
		return "Manteiner [ name=" + name + ", surname=" + surname + ", company=" + company + ", phoneNumber="
				+ phoneNumber + "mail=" + mail + "]";
	}
}
