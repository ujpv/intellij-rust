package org.rust.ide.inspections

/**
 * Tests for the CString Pointer inspection
 */
class RustCStringPointerInspectionTest : RustInspectionsTestBase(true) {

    fun testInspection() = checkByText<RustCStringPointerInspection>("""
        use std::ffi::{CString};

        fn main() {
            let s = CString::new("Hello").unwrap();
            let ptr1 = s.as_ptr();
            let ptr2 = <warning descr="Unsafe CString pointer">CString::new("World").unwrap().as_ptr()</warning>;
            unsafe {
                 *ptr1;
                 *ptr2;
            }
        }
    """)
}
