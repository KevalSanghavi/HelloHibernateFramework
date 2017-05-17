package project.package7;

public class Certificate implements Comparable<String> {

	private int id;
	private String name;

	public Certificate() {
	}

	public Certificate(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int compareTo(String that) {
		final int BEFORE = -1;
		final int AFTER = 1;

		if (that == null) {
			return BEFORE;
		}

		Comparable thisCertificate = this;
		Comparable thatCertificate = that;

		if (null == thisCertificate) {
			return AFTER;
		} else if (null == thatCertificate) {
			return BEFORE;
		} else {
			return thisCertificate.compareTo(thatCertificate);
		}
	}
	
}
