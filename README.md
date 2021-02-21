# jnr-ffi-array-pinning-tests

jnr-ffi-array-pinning-tests is a simple demonstration of `@Pinnable` array
params in `jnr-ffi`.

## Installation

Requires a build of `jnr-ffi` that enables array parameter pinning.

One can be built and installed via [pass-pinned-to-jffi in
bkgood/jnr-ffi](https://github.com/bkgood/jnr-ffi/tree/pass-pinned-to-jffi).
None, just clone.

## Usage

```bash
mvn test
```

On my system:

```
$ uname -a
Linux BILLTOP 5.4.72-microsoft-standard-WSL2 #1 SMP Wed Oct 28 23:40:43 UTC 2020 x86_64 x86_64 x86_64 GNU/Linux

$ less /proc/cpuinfo | grep name | head -n1
model name      : Intel(R) Core(TM) i7-5600U CPU @ 2.60GHz

$ $JAVA_HOME/bin/java -version
openjdk version "11.0.10" 2021-01-19
OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.10+9)
OpenJDK 64-Bit Server VM AdoptOpenJDK (build 11.0.10+9, mixed mode)
```

I see:

```
[INFO] Running com.github.bkgood.LibCTest
      1024: copying:        62960600 ns pinned:        27666600 ns diff: -56.06%
      2048: copying:        44469800 ns pinned:        25358700 ns diff: -42.98%
      4096: copying:        38833100 ns pinned:        18619400 ns diff: -52.05%
      8192: copying:        47651200 ns pinned:        19499800 ns diff: -59.08%
     16384: copying:        84001000 ns pinned:        19580200 ns diff: -76.69%
     32768: copying:       194749500 ns pinned:        21800300 ns diff: -88.81%
     65536: copying:       315342600 ns pinned:        18763600 ns diff: -94.05%
    131072: copying:       638674100 ns pinned:        19093800 ns diff: -97.01%
    262144: copying:      1475437500 ns pinned:        19991800 ns diff: -98.65%
    524288: copying:      3239485400 ns pinned:        19333300 ns diff: -99.40%
   1048576: copying:      6528736400 ns pinned:        18871500 ns diff: -99.71%
   2097152: copying:     16774023100 ns pinned:        19726000 ns diff: -99.88%
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 29.975 s - in com.github.bkgood.LibCTest
```

As buffer sizes increase, the costs of the Java heap-to-native copies for
unpinned array params grow to consume the vast majority of the time required
for a simple `memcpy` invocation (otherwise only copying a single byte).

## License
[Mozilla Public License Version 2.0](https://www.mozilla.org/en-US/MPL/2.0/)
