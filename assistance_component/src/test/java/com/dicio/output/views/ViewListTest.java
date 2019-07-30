package com.dicio.output.views;

import org.junit.Test;

import static org.junit.Assert.*;

public class ViewListTest {

    @Test
    public void testConstructorAndGetters() {
        Header header = new Header("header");
        Description description = new Description("desc");
        Image image = new Image("a,b", Image.SourceType.other);
        DescribedImage describedImage = new DescribedImage("a/b", Image.SourceType.local, "header2", "desc2");

        ViewList viewList = new ViewList(header, description, image, describedImage);
        assertArrayEquals(new BaseView[] {header, description, image, describedImage}, viewList.getViews());
    }

    @Test
    public void testListInception() {
        try {
            Object viewList1 = new ViewList();
            ViewList viewList2 = new ViewList((BaseView) viewList1);
            fail("List inception should not be allowed");
        } catch (Throwable e) {}
    }
}