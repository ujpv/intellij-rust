package org.rust.cargo.runconfig

import com.intellij.openapi.util.SystemInfo
import org.rust.cargo.util.modules

/**
 * Tests for RustBacktraceFilter
 */
class RustBacktraceFilterTest : HighlightFilterTestBase() {

    override fun getProjectDescriptor() = WithStdlibRustProjectDescriptor

    fun testRustcSourceCodeLink() =
        checkHighlights(filter,
            "          at src/main.rs:24",
            "          at [src/main.rs -> main.rs]:24")

    fun testRustcSourceCodeLinkWIthAbsolutePath() {
        // Windows does not handle abs paths on the tmpfs
        if (SystemInfo.isWindows) return
        val absPath = "${projectDir.canonicalPath}/src/main.rs"
        checkHighlights(filter,
            "          at $absPath:24",
            "          at [$absPath -> main.rs]:24")
    }

    fun testBacktraceLine() =
        checkHighlights(filter,
            "   7:     0x7feeefb7d11f - std::io::Stdin::read_line::h93df64e7370b5253",
            "   7:     0x7feeefb7d11f - [std::io::Stdin::read_line -> stdio.rs][::h93df64e7370b5253]")

    fun testBacktraceNoLinksToInternalCalls() =
        checkHighlights(filter,
            "  16:        0x106bfe616 - std::rt::lang_start::h538f8960e7644c80",
            "  16:        0x106bfe616 - std::rt::lang_start[::h538f8960e7644c80]")

    fun testBacktraceLineWithClosure() =
        checkHighlights(filter,
            " 103:     0x7feeefb480ee - std::io::Stdin::lock::{{closure}}::hbe9ea065746f6376",
            " 103:     0x7feeefb480ee - [std::io::Stdin::lock::{{closure}} -> stdio.rs][::hbe9ea065746f6376]")

    fun testBacktraceLineWithGenerics() =
        checkHighlights(filter,
            "  10:     0x70ae3fe9d6a3 - <core::option::Option<T>>::unwrap::h5dd7da6bb3d06020",
            "  10:     0x70ae3fe9d6a3 - [<core::option::Option<T>>::unwrap -> option.rs][::h5dd7da6bb3d06020]")

    fun testBacktraceNoLinksToUnknownFunctions() =
        checkNoHighlights(filter, "  17:        0x106c24240 - foo::bar::unknown[::h5dd7da6bb3d06020]")

    fun testBacktraceNotAppliedToInternalFunctions() =
        checkNoHighlights(filter, "  19:        0x106bf9a49 - main")

    fun testFullOutput() =
        checkHighlights(filter,
            """    Running `target/debug/test`
thread '<main>' panicked at 'called `Option::unwrap()` on a `None` value', ../src/libcore/option.rs:325
stack backtrace:
   1:     0x7feeefb45b1f - std::sys::backtrace::tracing::imp::write::h3800f45f421043b8
   2:     0x7feeefb47b93 - std::panicking::default_hook::hf3839060ccbb8764
   3:     0x7feeefb4095d - std::panicking::rust_panic_with_hook::h5dd7da6bb3d06020
   4:     0x7feeefb48151 - std::panicking::begin_panic::h9bf160aee246b9f6
   5:     0x7feeefb411fa - std::panicking::begin_panic_fmt::haf08a9a70a097ee1
   6:     0x7feeefb480ee - rust_begin_unwind
   7:     0x7feeefb7d11f - core::panicking::panic_fmt::h93df64e7370b5253
   8:     0x7feeefb7d3f8 - core::panicking::panic::h9d5bd65bbb401959
   9:     0x7feeefb3f765 - <core::option::Option<T>>::unwrap::hbe9ea065746f6376
                        at ../src/libcore/macros.rs:21
  10:     0x7feeefb3f538 - btest::main::h888e623968051ab6
                        at src/main.rs:22""",
            "                        at [src/main.rs -> main.rs]:22", 14)

    private val filter: RustBacktraceFilter get() = RustBacktraceFilter(project, projectDir, project.modules.first())

}
