package com.scriptfloor.scanvix.model;

import java.util.ArrayList;

/**
 * Created by LINCOLN on 3/22/2019.
 */

public interface PageTreeNode {
    public Page findByKey(String key);

    public void flattenCurrentPageSequence(ArrayList<Page> dest);
}
