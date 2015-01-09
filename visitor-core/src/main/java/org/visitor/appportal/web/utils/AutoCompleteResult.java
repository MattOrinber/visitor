/*

 * Template pack-mvc-3:src/main/java/web/util/AutoCompleteResult.p.vm.java
 */
package org.visitor.appportal.web.utils;

/**
 * Simple class that holds a primary key and a label for autocomplete
 */
public class AutoCompleteResult {
    private String id;
    private String label;
    private String label_1;
    private String label_2;
    private String label_3;
    private String label_4;

    public AutoCompleteResult(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

	public String getLabel_1() {
		return label_1;
	}

	public void setLabel_1(String label_1) {
		this.label_1 = label_1;
	}

	public String getLabel_2() {
		return label_2;
	}

	public void setLabel_2(String label_2) {
		this.label_2 = label_2;
	}

	public String getLabel_3() {
		return label_3;
	}

	public void setLabel_3(String label_3) {
		this.label_3 = label_3;
	}

	public String getLabel_4() {
		return label_4;
	}

	public void setLabel_4(String label_4) {
		this.label_4 = label_4;
	}
}