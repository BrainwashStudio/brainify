package com.brainwashstudio.orm.affinity;

import com.brainwashstudio.orm.TypeName;

public class NoneAffinity extends TypeName {

    @Override
    public String getName() {
        return "NONE";
    }
}
