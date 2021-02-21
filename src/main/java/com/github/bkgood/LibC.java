package com.github.bkgood;

import jnr.ffi.*;
import jnr.ffi.annotations.*;
import jnr.ffi.byref.*;
import jnr.ffi.mapper.*;
import jnr.ffi.types.*;
import jnr.ffi.Runtime;

public interface LibC {
    @IgnoreError
    Address memcpy(
            @Out byte[] dest,
            @In byte[] src,
            @size_t long len);

    @IgnoreError
    Address PINNED_memcpy(
            @Pinned byte[] dest,
            @Pinned byte[] src,
            @size_t long len);

    public static LibC load() {
        return LibraryLoader
            .create(LibC.class)
            // allow arbitrary [A-Z]+_ prefixes to distinguish between
            // different stubs for the same symbol with the same params.
            .mapper(new FunctionMapper() {
                @Override
                public String mapFunctionName(
                        String functionName,
                        FunctionMapper.Context ctx) {
                    if (!ctx.isSymbolPresent(functionName)) {
                        String noPrefix = functionName.replaceFirst(
                                "[A-Z]+_", "");

                        if (ctx.isSymbolPresent(noPrefix)) {
                            return noPrefix;
                        }
                    }

                    return functionName;
                }
            })
            .load("c");
    }
}
