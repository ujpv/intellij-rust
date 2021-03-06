package org.rust.lang.core.stubs

import com.intellij.psi.stubs.*
import org.rust.lang.core.psi.*
import org.rust.lang.core.psi.impl.*
import org.rust.lang.core.psi.impl.mixin.asRustPath
import org.rust.lang.core.psi.impl.mixin.isLocal
import org.rust.lang.core.psi.impl.mixin.pathAttribute
import org.rust.lang.core.symbols.RustPath
import org.rust.lang.core.symbols.readRustPath
import org.rust.lang.core.symbols.writeRustPath
import org.rust.lang.core.types.RustUnknownType
import org.rust.lang.core.types.unresolved.RustUnresolvedType
import org.rust.lang.core.types.unresolved.readRustUnresolvedType
import org.rust.lang.core.types.unresolved.writeRustUnresolvedType
import org.rust.lang.core.types.util.type
import org.rust.utils.readNullable
import org.rust.utils.writeNullable


fun factory(name: String): RustStubElementType<*, *> = when (name) {
    "EXTERN_CRATE_ITEM" -> RustExternCrateItemElementStub.Type
    "USE_ITEM" -> RustUseItemElementStub.Type

    "CONST_ITEM" -> RustConstItemElementStub.Type
    "STATIC_ITEM" -> RustStaticItemElementStub.Type

    "STRUCT_ITEM" -> RustStructItemElementStub.Type
    "ENUM_ITEM" -> RustEnumItemElementStub.Type
    "UNION_ITEM" -> RustUnionItemElementStub.Type

    "MOD_DECL_ITEM" -> RustModDeclItemElementStub.Type
    "MOD_ITEM" -> RustModItemElementStub.Type

    "TRAIT_ITEM" -> RustTraitItemElementStub.Type
    "IMPL_ITEM" -> RustImplItemElementStub.Type

    "FN_ITEM" -> RustFnItemElementStub.Type
    "TYPE_ITEM" -> RustTypeItemElementStub.Type
    "FOREIGN_MOD_ITEM" -> RustForeignModItemElementStub.Type

    "FIELD_DECL" -> RustFieldDeclElementStub.Type
    "IMPL_METHOD_MEMBER" -> RustImplMethodMemberElementStub.Type
    "TRAIT_METHOD_MEMBER" -> RustTraitMethodMemberElementStub.Type

    else -> error("Unknown element $name")
}


class RustExternCrateItemElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>,
    override val name: String?,
    override val isPublic: Boolean
) : StubBase<RustExternCrateItemElement>(parent, elementType),
    RustNamedStub,
    RustVisibilityStub {

    object Type : RustStubElementType<RustExternCrateItemElementStub, RustExternCrateItemElement>("EXTERN_CRATE_ITEM") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustExternCrateItemElementStub(parentStub, this,
                dataStream.readNameAsString(),
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustExternCrateItemElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.name)
                writeBoolean(stub.isPublic)
            }

        override fun createPsi(stub: RustExternCrateItemElementStub) =
            RustExternCrateItemElementImpl(stub, this)

        override fun createStub(psi: RustExternCrateItemElement, parentStub: StubElement<*>?) =
            RustExternCrateItemElementStub(parentStub, this, psi.name, psi.isPublic)

        override fun indexStub(stub: RustExternCrateItemElementStub, sink: IndexSink) = sink.indexExternCrate(stub)
    }
}


class RustUseItemElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>,
    val alias: String?,
    override val isPublic: Boolean
) : RustElementStub<RustUseItemElement>(parent, elementType),
    RustVisibilityStub {

    object Type : RustStubElementType<RustUseItemElementStub, RustUseItemElement>("USE_ITEM") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustUseItemElementStub(parentStub, this,
                dataStream.readNameAsString(),
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustUseItemElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.alias)
                writeBoolean(stub.isPublic)
            }

        override fun createPsi(stub: RustUseItemElementStub) =
            RustUseItemElementImpl(stub, this)

        override fun createStub(psi: RustUseItemElement, parentStub: StubElement<*>?) =
            RustUseItemElementStub(parentStub, this, psi.alias?.name, psi.isPublic)

        override fun indexStub(stub: RustUseItemElementStub, sink: IndexSink) = sink.indexUseItem(stub)
    }
}


class RustConstItemElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>,
    override val name: String?,
    override val isPublic: Boolean
) : StubBase<RustConstItemElement>(parent, elementType),
    RustNamedStub,
    RustVisibilityStub {

    object Type : RustStubElementType<RustConstItemElementStub, RustConstItemElement>("CONST_ITEM") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustConstItemElementStub(parentStub, this,
                dataStream.readNameAsString(),
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustConstItemElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.name)
                writeBoolean(stub.isPublic)
            }

        override fun createPsi(stub: RustConstItemElementStub) =
            RustConstItemElementImpl(stub, this)

        override fun createStub(psi: RustConstItemElement, parentStub: StubElement<*>?) =
            RustConstItemElementStub(parentStub, this, psi.name, psi.isPublic)


        override fun indexStub(stub: RustConstItemElementStub, sink: IndexSink) = sink.indexConstItem(stub)
    }
}


class RustStaticItemElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>,
    override val name: String?,
    override val isPublic: Boolean
) : StubBase<RustStaticItemElement>(parent, elementType),
    RustNamedStub,
    RustVisibilityStub {

    object Type : RustStubElementType<RustStaticItemElementStub, RustStaticItemElement>("STATIC_ITEM") {
        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustStaticItemElementStub(parentStub, this,
                dataStream.readNameAsString(),
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustStaticItemElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.name)
                writeBoolean(stub.isPublic)
            }

        override fun createPsi(stub: RustStaticItemElementStub) =
            RustStaticItemElementImpl(stub, this)

        override fun createStub(psi: RustStaticItemElement, parentStub: StubElement<*>?) =
            RustStaticItemElementStub(parentStub, this, psi.name, psi.isPublic)

        override fun indexStub(stub: RustStaticItemElementStub, sink: IndexSink) = sink.indexStaticItem(stub)
    }
}


class RustStructItemElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>,
    override val name: String?,
    override val isPublic: Boolean
) : StubBase<RustStructItemElement>(parent, elementType),
    RustNamedStub,
    RustVisibilityStub {

    object Type : RustStubElementType<RustStructItemElementStub, RustStructItemElement>("STRUCT_ITEM") {
        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustStructItemElementStub(parentStub, this,
                dataStream.readNameAsString(),
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustStructItemElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.name)
                writeBoolean(stub.isPublic)
            }

        override fun createPsi(stub: RustStructItemElementStub): RustStructItemElement =
            RustStructItemElementImpl(stub, this)

        override fun createStub(psi: RustStructItemElement, parentStub: StubElement<*>?) =
            RustStructItemElementStub(parentStub, this, psi.name, psi.isPublic)


        override fun indexStub(stub: RustStructItemElementStub, sink: IndexSink) = sink.indexStructItem(stub)
    }
}


class RustEnumItemElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>,
    override val name: String?,
    override val isPublic: Boolean
) : StubBase<RustEnumItemElement>(parent, elementType),
    RustNamedStub,
    RustVisibilityStub {

    object Type : RustStubElementType<RustEnumItemElementStub, RustEnumItemElement>("ENUM_ITEM") {
        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): RustEnumItemElementStub =
            RustEnumItemElementStub(parentStub, this,
                dataStream.readNameAsString(),
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustEnumItemElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.name)
                writeBoolean(stub.isPublic)
            }

        override fun createPsi(stub: RustEnumItemElementStub) =
            RustEnumItemElementImpl(stub, this)

        override fun createStub(psi: RustEnumItemElement, parentStub: StubElement<*>?) =
            RustEnumItemElementStub(parentStub, this, psi.name, psi.isPublic)


        override fun indexStub(stub: RustEnumItemElementStub, sink: IndexSink) = sink.indexEnumItem(stub)

    }
}


class RustUnionItemElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>,
    override val name: String?,
    override val isPublic: Boolean
) : StubBase<RustUnionItemElement>(parent, elementType),
    RustNamedStub,
    RustVisibilityStub {

    object Type : RustStubElementType<RustUnionItemElementStub, RustUnionItemElement>("UNION_ITEM") {
        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustUnionItemElementStub(parentStub, this,
                dataStream.readNameAsString(),
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustUnionItemElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.name)
                writeBoolean(stub.isPublic)
            }

        override fun createPsi(stub: RustUnionItemElementStub) =
            RustUnionItemElementImpl(stub, this)

        override fun createStub(psi: RustUnionItemElement, parentStub: StubElement<*>?) =
            RustUnionItemElementStub(parentStub, this, psi.name, psi.isPublic)

        override fun indexStub(stub: RustUnionItemElementStub, sink: IndexSink) = sink.indexUnionItem(stub)
    }
}


class RustModDeclItemElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>,
    override val name: String?,
    override val isPublic: Boolean,
    val pathAttribute: String?,
    val isLocal: Boolean
) : StubBase<RustModDeclItemElement>(parent, elementType),
    RustNamedStub,
    RustVisibilityStub {

    object Type : RustStubElementType<RustModDeclItemElementStub, RustModDeclItemElement>("MOD_DECL_ITEM") {
        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustModDeclItemElementStub(parentStub, this,
                dataStream.readNameAsString(),
                dataStream.readBoolean(),
                dataStream.readUTFFast().let { if (it == "") null else it },
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustModDeclItemElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.name)
                writeBoolean(stub.isPublic)
                writeUTFFast(stub.pathAttribute ?: "")
                writeBoolean(stub.isLocal)
            }

        override fun createPsi(stub: RustModDeclItemElementStub) =
            RustModDeclItemElementImpl(stub, this)

        override fun createStub(psi: RustModDeclItemElement, parentStub: StubElement<*>?) =
            RustModDeclItemElementStub(parentStub, this, psi.name, psi.isPublic, psi.pathAttribute, psi.isLocal)

        override fun indexStub(stub: RustModDeclItemElementStub, sink: IndexSink) = sink.indexModDeclItem(stub)
    }
}


class RustModItemElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>,
    override val name: String?,
    override val isPublic: Boolean
) : StubBase<RustModItemElement>(parent, elementType),
    RustNamedStub,
    RustVisibilityStub {

    object Type : RustStubElementType<RustModItemElementStub, RustModItemElement>("MOD_ITEM") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustModItemElementStub(parentStub, this,
                dataStream.readNameAsString(),
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustModItemElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.name)
                writeBoolean(stub.isPublic)
            }

        override fun createPsi(stub: RustModItemElementStub): RustModItemElement =
            RustModItemElementImpl(stub, this)

        override fun createStub(psi: RustModItemElement, parentStub: StubElement<*>?) =
            RustModItemElementStub(parentStub, this, psi.name, psi.isPublic)

        override fun indexStub(stub: RustModItemElementStub, sink: IndexSink) = sink.indexModItem(stub)
    }
}


class RustTraitItemElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>,
    override val name: String?,
    override val isPublic: Boolean
) : StubBase<RustTraitItemElement>(parent, elementType),
    RustNamedStub,
    RustVisibilityStub {

    object Type : RustStubElementType<RustTraitItemElementStub, RustTraitItemElement>("TRAIT_ITEM") {
        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustTraitItemElementStub(parentStub, this,
                dataStream.readNameAsString(),
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustTraitItemElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.name)
                writeBoolean(stub.isPublic)
            }

        override fun createPsi(stub: RustTraitItemElementStub): RustTraitItemElement =
            RustTraitItemElementImpl(stub, this)

        override fun createStub(psi: RustTraitItemElement, parentStub: StubElement<*>?) =
            RustTraitItemElementStub(parentStub, this, psi.name, psi.isPublic)

        override fun indexStub(stub: RustTraitItemElementStub, sink: IndexSink) = sink.indexTraitItem(stub)
    }
}


class RustImplItemElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>,
    val type: RustUnresolvedType,
    val traitRef: RustPath?
) : RustElementStub<RustImplItemElement>(parent, elementType) {
    object Type : RustStubElementType<RustImplItemElementStub, RustImplItemElement>("IMPL_ITEM") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustImplItemElementStub(parentStub, this,
                dataStream.readRustUnresolvedType(),
                dataStream.readNullable { readRustPath() }
            )

        override fun serialize(stub: RustImplItemElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeRustUnresolvedType(stub.type)
                writeNullable(stub.traitRef) { writeRustPath(it) }
            }

        override fun createPsi(stub: RustImplItemElementStub): RustImplItemElement =
            RustImplItemElementImpl(stub, this)

        override fun createStub(psi: RustImplItemElement, parentStub: StubElement<*>?) =
            RustImplItemElementStub(parentStub, this,
                psi.type?.type ?: RustUnknownType, psi.traitRef?.path?.asRustPath)

        override fun indexStub(stub: RustImplItemElementStub, sink: IndexSink) = sink.indexImplItem(stub)
    }
}


class RustFnItemElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>,
    override val name: String?,
    override val isPublic: Boolean,
    override val isAbstract: Boolean,
    override val isStatic: Boolean,
    override val isTest: Boolean
) : StubBase<RustFnItemElement>(parent, elementType),
    RustNamedStub,
    RustVisibilityStub,
    RustFnStub {

    object Type : RustStubElementType<RustFnItemElementStub, RustFnItemElement>("FN_ITEM") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustFnItemElementStub(parentStub, this,
                dataStream.readName()?.string,
                dataStream.readBoolean(),
                dataStream.readBoolean(),
                dataStream.readBoolean(),
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustFnItemElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.name)
                writeBoolean(stub.isPublic)
                writeBoolean(stub.isAbstract)
                writeBoolean(stub.isStatic)
                writeBoolean(stub.isTest)
            }

        override fun createPsi(stub: RustFnItemElementStub) =
            RustFnItemElementImpl(stub, this)

        override fun createStub(psi: RustFnItemElement, parentStub: StubElement<*>?) =
            RustFnItemElementStub(parentStub, this,
                psi.name, psi.isPublic, psi.isAbstract, psi.isStatic, psi.isTest)

        override fun indexStub(stub: RustFnItemElementStub, sink: IndexSink) = sink.indexFnItem(stub)
    }
}


class RustTypeItemElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>,
    override val name: String?,
    override val isPublic: Boolean
) : StubBase<RustTypeItemElement>(parent, elementType),
    RustNamedStub,
    RustVisibilityStub {

    object Type : RustStubElementType<RustTypeItemElementStub, RustTypeItemElement>("TYPE_ITEM") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustTypeItemElementStub(parentStub, this,
                dataStream.readNameAsString(),
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustTypeItemElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.name)
                writeBoolean(stub.isPublic)
            }

        override fun createPsi(stub: RustTypeItemElementStub) =
            RustTypeItemElementImpl(stub, this)

        override fun createStub(psi: RustTypeItemElement, parentStub: StubElement<*>?) =
            RustTypeItemElementStub(parentStub, this, psi.name, psi.isPublic)

        override fun indexStub(stub: RustTypeItemElementStub, sink: IndexSink) = sink.indexTypeItem(stub)
    }
}


class RustForeignModItemElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>
) : StubBase<RustForeignModItemElement>(parent, elementType) {

    object Type : RustStubElementType<RustForeignModItemElementStub, RustForeignModItemElement>("FOREIGN_MOD_ITEM") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustForeignModItemElementStub(parentStub, this)

        override fun serialize(stub: RustForeignModItemElementStub, dataStream: StubOutputStream) {
            // NOP
        }

        override fun createPsi(stub: RustForeignModItemElementStub) =
            RustForeignModItemElementImpl(stub, this)

        override fun createStub(psi: RustForeignModItemElement, parentStub: StubElement<*>?) =
            RustForeignModItemElementStub(parentStub, this)

        override fun indexStub(stub: RustForeignModItemElementStub, sink: IndexSink) {
            // NOP
        }
    }
}


class RustFieldDeclElementStub(
    parent: StubElement<*>?, elementType: IStubElementType<*, *>,
    override val name: String?,
    override val isPublic: Boolean
) : StubBase<RustFieldDeclElement>(parent, elementType),
    RustNamedStub,
    RustVisibilityStub {

    object Type : RustStubElementType<RustFieldDeclElementStub, RustFieldDeclElement>("FIELD_DECL") {
        override fun createPsi(stub: RustFieldDeclElementStub) =
            RustFieldDeclElementImpl(stub, this)

        override fun createStub(psi: RustFieldDeclElement, parentStub: StubElement<*>?) =
            RustFieldDeclElementStub(parentStub, this, psi.name, psi.isPublic)

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustFieldDeclElementStub(parentStub, this,
                dataStream.readNameAsString(),
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustFieldDeclElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.name)
                writeBoolean(stub.isPublic)
            }

        override fun indexStub(stub: RustFieldDeclElementStub, sink: IndexSink) = sink.indexFieldDecl(stub)
    }
}


class RustImplMethodMemberElementStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
    override val name: String?,
    override val isPublic: Boolean,
    override val isAbstract: Boolean,
    override val isStatic: Boolean,
    override val isTest: Boolean
) : StubBase<RustImplMethodMemberElement>(parent, elementType),
    RustNamedStub,
    RustVisibilityStub,
    RustFnStub {

    object Type : RustStubElementType<RustImplMethodMemberElementStub, RustImplMethodMemberElement>("IMPL_METHOD_MEMBER") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustImplMethodMemberElementStub(parentStub, this,
                dataStream.readNameAsString(),
                dataStream.readBoolean(),
                dataStream.readBoolean(),
                dataStream.readBoolean(),
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustImplMethodMemberElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.name)
                writeBoolean(stub.isPublic)
                writeBoolean(stub.isAbstract)
                writeBoolean(stub.isStatic)
                writeBoolean(stub.isTest)
            }

        override fun createPsi(stub: RustImplMethodMemberElementStub) =
            RustImplMethodMemberElementImpl(stub, this)

        override fun createStub(psi: RustImplMethodMemberElement, parentStub: StubElement<*>?) =
            RustImplMethodMemberElementStub(parentStub, this,
                psi.name, psi.isPublic, psi.isAbstract, psi.isStatic, psi.isTest)

        override fun indexStub(stub: RustImplMethodMemberElementStub, sink: IndexSink) = sink.indexImplMethodMember(stub)
    }
}


class RustTraitMethodMemberElementStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
    override val name: String?,
    override val isAbstract: Boolean,
    override val isStatic: Boolean,
    override val isTest: Boolean

) : StubBase<RustTraitMethodMemberElement>(parent, elementType),
    RustNamedStub,
    RustFnStub {

    object Type : RustStubElementType<RustTraitMethodMemberElementStub, RustTraitMethodMemberElement>("TRAIT_METHOD_MEMBER") {
        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            RustTraitMethodMemberElementStub(parentStub, this,
                dataStream.readNameAsString(),
                dataStream.readBoolean(),
                dataStream.readBoolean(),
                dataStream.readBoolean()
            )

        override fun serialize(stub: RustTraitMethodMemberElementStub, dataStream: StubOutputStream) =
            with(dataStream) {
                writeName(stub.name)
                writeBoolean(stub.isAbstract)
                writeBoolean(stub.isStatic)
                writeBoolean(stub.isTest)
            }

        override fun createPsi(stub: RustTraitMethodMemberElementStub): RustTraitMethodMemberElement =
            RustTraitMethodMemberElementImpl(stub, this)

        override fun createStub(psi: RustTraitMethodMemberElement, parentStub: StubElement<*>?) =
            RustTraitMethodMemberElementStub(parentStub, this,
                psi.name, psi.isAbstract, psi.isStatic, psi.isTest)

        override fun indexStub(stub: RustTraitMethodMemberElementStub, sink: IndexSink) = sink.indexTraitMethodMember(stub)
    }
}
