package org.rust.lang.core.psi

/**
 * Type-bearing element is actually an element designating entity that may be constituent of
 * some type.
 *
 * Typical residents are: [RustStructItemElement], [RustEnumItemElement], [RustFnItemElement], etc.
 */
interface RustTypeBearingItemElement : RustItemElement, RustNamedElement
