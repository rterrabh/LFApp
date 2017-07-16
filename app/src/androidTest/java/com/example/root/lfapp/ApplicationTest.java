package com.example.root.lfapp;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.text.Html;

import com.ufla.lfapp.utils.UtilSpannableTags;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {

        super(Application.class);
        String str = "<b>#2</b> Se [q<sub><small>j</small></sub>, B] ∈ <i>δ\\'</i>(q<sub><small>i</small></sub>, x, A), então\\t(A e B ∈ Γ U {λ})\\n\n" +
                "\\t<b><bullet></b><q<sub><small>i</small></sub>, A, <cb:#786DBE>q<sub><small>k</small></sub></cb>> → x<q<sub><small>j</small></sub>, B, <cb:#786DBE>q<sub><small>k</small></sub></colorBack>>, para todo <cb:#786DBE>q<sub><small>k</small></sub></colorBack> ∈ Q\\n\n" +
                "<b>#3</b> Se [q<sub><small>j</small></sub>, BA] ∈ <i>δ\\'</i>(q<sub><small>i</small></sub>, x, A), então\\t(A e B ∈ Γ)\\n\n" +
                "\\t<b><bullet></b> <qi, A, <cb:#786DBE>q<sub><small>k</small></sub></colorBack>> → x<q<sub><small>j</small></sub>, B, q<sub><small>n</small></sub>><<cb:#8FD45A>q<sub><small>n</small></sub></cb>, A, <cb:#786DBE>q<sub><small>k</small></sub></cb>>,<cb:#786DBE>q<sub><small>k</small></sub></cb>, <cb:#8FD45A>q<sub><small>n</small></sub></colorBack> ∈ Q\\n\n";
                System.out.println(Html.fromHtml(str));
        try {
            System.out.println(UtilSpannableTags.decode(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}