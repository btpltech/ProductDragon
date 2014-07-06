package com.ample.mapping;

public class FloatValueExtractionFromString {
	public static void main(String[] args) {

		String str = "INR..............,,,,,,,,,,,,,80909809809809############127.0101090909";
		String res = " ";

		int countDot = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if ((c < '0' || c > '9') && c != '.')
				continue;
			else {
				if (i > 1) {
					if ((str.charAt(i - 1) < '0' || str.charAt(i - 1) > '9')
							&& str.charAt(i - 1) != '.' && res != " ") {

						break;
					} else {
						if (c == '.') {
							if (countDot == 0 && res != " ") {
								res += c;
								// System.out.println("ind");
								countDot++;
							} else if (res != " ")
								break;
						} else {
							res += c;
							// System.out.println("indo");
						}

					}
				} else {
					if (c == '.' && res == " ")
						continue;
					else
						res += c;
					// System.out.println("in");
				}

			}
		}
		res = res.replaceAll(" ", "");
		System.out.println(Double.parseDouble(res));
	}
}
