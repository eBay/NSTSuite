package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock;

import java.util.Comparator;

public class SmallestToLargestArrayPathComparator implements Comparator<String>  {

    private static final String REGEX_PATTERN = "\\[.*\\]";

    @Override
    public int compare(String o1, String o2) {

        // Shallower Json Path, based on number of steps, (o1 fewer steps than o2) will return less than (-1), (o1 more steps than o2) will return (1)
        // If two paths are not identical, but contain the same number of steps, then o1 will be greater than o2. (1)
        // Equals will only apply if paths are the same length and contain the same exact values, including index values (0).

        if (o1.equals(o2)) {
            return 0;
        }

        String[] o1split = o1.split("\\.");
        String[] o2split = o2.split("\\.");

        if (o1split.length == o2split.length) {
            o1 = o1.replaceAll(REGEX_PATTERN, "");
            o2 = o2.replaceAll(REGEX_PATTERN, "");
            return o1.compareTo(o2);
        }

        if (o1split.length < o2split.length) {
            return -1;
        }

        return 1;
    }
}
