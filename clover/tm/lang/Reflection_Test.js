var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":31,"id":303,"methods":[{"el":17,"sc":2,"sl":13},{"el":30,"sc":2,"sl":19}],"name":"Reflection_Test","sl":8}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_21":{"methods":[{"sl":19}],"name":"Reflection_Ctor","pass":true,"statements":[{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":26},{"sl":27},{"sl":28},{"sl":29}]},"test_3":{"methods":[{"sl":19}],"name":"Reflection_Ctor","pass":true,"statements":[{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":26},{"sl":27},{"sl":28},{"sl":29}]},"test_5":{"methods":[{"sl":19}],"name":"Reflection_Ctor","pass":true,"statements":[{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":26},{"sl":27},{"sl":28},{"sl":29}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [5, 3, 21], [], [], [5, 3, 21], [5, 3, 21], [5, 3, 21], [5, 3, 21], [5, 3, 21], [5, 3, 21], [5, 3, 21], [5, 3, 21], [], []]