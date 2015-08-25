package com.gluo.pruebas.tests;

import com.gluo.pruebas.DefaultFragment;
import com.gluo.pruebas.test1.LocationFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TestContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<TestItem> ITEMS = new ArrayList<TestItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, TestItem> ITEM_MAP = new HashMap<String, TestItem>();

    static {
        // Add 3 sample items.
        addItem(new TestItem("1", "Test 1 - Location", new LocationFragment()));
        addItem(new TestItem("2", "Test 2"));
        addItem(new TestItem("3", "Test 3"));
    }

    private static void addItem(TestItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class TestItem {
        public String id;
        public String content;
        public DefaultFragment fragmentClass;

        public TestItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        public TestItem(String id, String content, DefaultFragment fragmentClass) {
            this.id = id;
            this.content = content;
            this.fragmentClass = fragmentClass;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
