var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":26,"id":89,"methods":[{"el":11,"sc":2,"sl":8},{"el":16,"sc":2,"sl":13},{"el":20,"sc":2,"sl":17},{"el":25,"sc":2,"sl":22}],"name":"TestObject","sl":3}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_14":{"methods":[{"sl":8},{"sl":13},{"sl":17},{"sl":22}],"name":"invoke","pass":true,"statements":[{"sl":10},{"sl":15},{"sl":19},{"sl":24}]},"test_18":{"methods":[{"sl":8},{"sl":13},{"sl":17},{"sl":22}],"name":"invoke","pass":true,"statements":[{"sl":10},{"sl":15},{"sl":19},{"sl":24}]},"test_2":{"methods":[{"sl":8},{"sl":13},{"sl":17},{"sl":22}],"name":"invoke","pass":true,"statements":[{"sl":10},{"sl":15},{"sl":19},{"sl":24}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [18, 2, 14], [], [18, 2, 14], [], [], [18, 2, 14], [], [18, 2, 14], [], [18, 2, 14], [], [18, 2, 14], [], [], [18, 2, 14], [], [18, 2, 14], [], []]