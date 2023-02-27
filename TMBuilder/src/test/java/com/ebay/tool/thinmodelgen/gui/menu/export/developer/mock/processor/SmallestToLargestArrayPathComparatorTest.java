package com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor;

import com.ebay.tool.thinmodelgen.gui.menu.export.developer.mock.processor.SmallestToLargestArrayPathComparator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.TreeMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SmallestToLargestArrayPathComparatorTest {

    private SmallestToLargestArrayPathComparator comparator = new SmallestToLargestArrayPathComparator();

    @DataProvider(name = "compareTestValues")
    public Object[][] compareTestValues() {

        return new Object[][]{
                { "$.root.step", "$.root.step", 0 },
                { "$.root.step", "$.root.step[*]", 0 },
                { "$.root.step[0]", "$.root.step[*]", 0 },
                { "$.root", "$.root.step", -1 },
                { "$.root[1]", "$.root.step", -1},
                { "$.root.step", "$.root", 1},
                { "$.root.alt", "$.root.step", -18},
                { "$.root.step", "$.root.alt", 18}
        };
    }

    @Test(dataProvider = "compareTestValues")
    public void testCompare(String o1, String o2, int expected) {
        int actual = comparator.compare(o1, o2);
        assertThat(String.format("o1 [%s] compare to o2 [$s] did not match expected result.", o1, o2), actual, is(equalTo(expected)));
    }

    @Test
    public void treeMapTest() {
        String a = "$.first.second.third";
        String b = "$.first.second.alternate";
        String c = "$.first.second.third.name";

        TreeMap<String, Integer> tree = new TreeMap<>();
        tree.put(a, 1);
        tree.put(b, 2);
        tree.put(c, 3);

        Integer val = tree.get(a);
        assertThat(val, is(equalTo(1)));

        val = tree.get(b);
        assertThat(val, is(equalTo(2)));

        val = tree.get(c);
        assertThat(val, is(equalTo(3)));
    }
}