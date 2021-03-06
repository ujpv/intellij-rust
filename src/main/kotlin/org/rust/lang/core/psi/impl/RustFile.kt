package org.rust.lang.core.psi.impl

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import org.rust.cargo.project.workspace.cargoProject
import org.rust.lang.RustFileType
import org.rust.lang.RustLanguage
import org.rust.lang.core.psi.RustInnerAttrElement
import org.rust.lang.core.psi.RustInnerAttributeOwner
import org.rust.lang.core.psi.RustMod
import org.rust.lang.core.psi.util.module
import org.rust.lang.core.resolve.ref.RustReference
import org.rust.lang.core.stubs.index.RustModulesIndex
import org.rust.lang.core.symbols.RustPath

class RustFile(
    fileViewProvider: FileViewProvider
) : PsiFileBase(fileViewProvider, RustLanguage),
    RustMod,
    RustInnerAttributeOwner {

    override fun getReference(): RustReference? = null

    override fun getFileType(): FileType = RustFileType

    override val `super`: RustMod? get() {
        val original = originalFile as RustFile
        return CachedValuesManager.getCachedValue(original, CachedValueProvider {
            CachedValueProvider.Result.create(
                RustModulesIndex.getSuperFor(original),
                PsiModificationTracker.MODIFICATION_COUNT
            )
        })
    }

    override val modName: String? = if (name != RustMod.MOD_RS) FileUtil.getNameWithoutExtension(name) else parent?.name

    override val crateRelativePath: RustPath.CrateRelative? get() = RustPsiImplUtil.modCrateRelativePath(this)

    override val ownsDirectory: Boolean
        get() = name == RustMod.MOD_RS || isCrateRoot

    override val ownedDirectory: PsiDirectory?
        get() = originalFile.parent

    override val isCrateRoot: Boolean get() {
        val file = originalFile.virtualFile ?: return false
        return module?.cargoProject?.isCrateRoot(file) ?: false
    }

    override val innerAttrList: List<RustInnerAttrElement>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, RustInnerAttrElement::class.java)

}


val PsiFile.rustMod: RustMod? get() = this as? RustFile

val VirtualFile.isNotRustFile: Boolean get() = !isRustFile
val VirtualFile.isRustFile: Boolean get() = fileType == RustFileType
