// This is a generated file. Not intended for manual editing.
package dev.meanmail.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import dev.meanmail.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeoBlockContentImpl extends ASTWrapperPsiElement implements GeoBlockContent {

    public GeoBlockContentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull Visitor visitor) {
        visitor.visitGeoBlockContent(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof Visitor) accept((Visitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public GeoDefaultStmt getGeoDefaultStmt() {
        return findChildByClass(GeoDefaultStmt.class);
    }

    @Override
    @Nullable
    public GeoDeleteStmt getGeoDeleteStmt() {
        return findChildByClass(GeoDeleteStmt.class);
    }

    @Override
    @Nullable
    public GeoIncludeStmt getGeoIncludeStmt() {
        return findChildByClass(GeoIncludeStmt.class);
    }

    @Override
    @Nullable
    public GeoProxyStmt getGeoProxyStmt() {
        return findChildByClass(GeoProxyStmt.class);
    }

    @Override
    @Nullable
    public GeoRangesStmt getGeoRangesStmt() {
        return findChildByClass(GeoRangesStmt.class);
    }

    @Override
    @Nullable
    public GeoValueStmt getGeoValueStmt() {
        return findChildByClass(GeoValueStmt.class);
    }

}
