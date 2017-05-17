package project.package7;

import java.util.Comparator;

public class MyComparatorClass implements Comparator<String> {

	public int compare(String o1, String o2) {
		final int BEFORE = -1;
		final int AFTER = 1;

		/* To reverse the sorting order, multiple by -1 */
		if (o2 == null) {
			return BEFORE * -1;
		}

		Comparable thisCertificate = o1;
		Comparable thatCertificate = o2;

		if (thisCertificate == null) {
			return AFTER * 1;
		} else if (thatCertificate == null) {
			return BEFORE * -1;
		} else {
			return thisCertificate.compareTo(thatCertificate) * -1;
		}
	}
}
