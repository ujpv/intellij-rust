package org.rust.lang.core.psi.impl.mixin

import com.intellij.lang.ASTNode
import org.rust.ide.icons.RustIcons
import org.rust.lang.core.psi.RustEnumItemElement
import org.rust.lang.core.psi.RustEnumVariantElement
import org.rust.lang.core.psi.impl.RustNamedElementImpl
import org.rust.lang.core.symbols.RustPath
import org.rust.lang.core.symbols.RustPathSegment
import javax.swing.Icon


abstract class RustEnumVariantImplMixin(node: ASTNode) : RustNamedElementImpl(node), RustEnumVariantElement {
    override fun getIcon(flags: Int): Icon = RustIcons.ENUM_VARIANT

    override val crateRelativePath: RustPath.CrateRelative? get() {
        val variantName = name ?: return null
        return parentEnum.crateRelativePath?.join(RustPathSegment.withoutGenerics(variantName))
    }
}

val RustEnumVariantElement.parentEnum: RustEnumItemElement get() = parent?.parent as RustEnumItemElement
