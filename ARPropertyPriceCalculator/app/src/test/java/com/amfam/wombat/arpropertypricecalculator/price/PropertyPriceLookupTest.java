package com.amfam.wombat.arpropertypricecalculator.price;

import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyPriceLookupTest {

    @Test
    public void getPrice() {
        PropertyPriceLookup lookup = new PropertyPriceLookup();
        System.out.println(lookup.getPrice("test"));

    }
}