## Initial and Final Permutations

`INITIAL_PERMUATIONS` & `FINAL_PERMUATIONS` are inverse images of each other.

```java
INITIAL_PERMUATIONS[FINAL_PERMUTATION[idx]] = idx;

OR

FINAL_PERMUATIONS[INITIAL_PERMUTATION[idx]] = idx;

```

## S-BOX

There are 8 S-BOX(s) in DES, they are being used to reduce 6-bit into 4-bit value.
Since maximum number in any S-BOX is 15.
```
2^4 - 1 = 15
```

