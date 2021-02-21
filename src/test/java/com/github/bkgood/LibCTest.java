package com.github.bkgood;

import jnr.ffi.*;
import jnr.ffi.annotations.*;
import jnr.ffi.byref.*;
import jnr.ffi.types.*;
import jnr.ffi.Runtime;

import org.junit.Assert;

import org.junit.Test;

public class LibCTest 
{
    @Test
    public void emptyMemcpy() {
        LibC libc = LibC.load();

        int iters = 100_000;

        byte[] dest = new byte[1];

        for (int size = 1024; size < 1 << 22; size <<= 1) {
            byte[] src = new byte[size];

            System.out.printf("% 10d: ", size);

            final long timeCopying;

            {
                long t0 = System.nanoTime();

                for (int i = 0; i < iters; i++) {
                    src[0]++;

                    libc.memcpy(dest, src, 1);

                    Assert.assertEquals(src[0], dest[0]);
                }

                timeCopying = System.nanoTime() - t0;

                System.out.printf(
                        "copying: % 15d ns ",
                        timeCopying);
            }

            final long timePinned;

            {
                long t0 = System.nanoTime();

                for (int i = 0; i < iters; i++) {
                    src[0]++;

                    libc.PINNED_memcpy(dest, src, 1);

                    Assert.assertEquals(src[0], dest[0]);
                }

                timePinned = System.nanoTime() - t0;

                System.out.printf(
                        "pinned: % 15d ns ",
                        timePinned);
            }

            System.out.printf(
                    "diff: %.2f%%\n",
                    (timePinned - timeCopying) / (double) timeCopying * 100.0);
        }

    }
}

