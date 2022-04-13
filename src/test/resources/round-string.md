### keyword

- for
- in
- as
- break
- continue
- if
- elseif
- else
- end
- and
- or
- import
- include
- function
- macro
- assign
- nested

### builtin command

- @start: start a session
- @end: end a session
- @delete: perform a action

### loop statement

```
${for (item1, index1) in list1}
    list1 has a item ${list1[index1]} on index ${index1}

    # delete the previous two lines
    ${@delete previous_line 2}
    ${for item2 in list2}
        loop-style like `for(i=0;i<n;i++)` is unsupported
    ${end}
    
    ${for (item3, index3) in list3 @trim @trim ', '}
        $item3, 
    ${end}
    
    ${for (item4, index4, sep as ',') in list4 @trim}
        $item4$sep 
    ${end}
${end}
```

### condition statement

```
${if model.type == 'modify' @start trim}
    mutable
${elseif model.type == 'query'}
    immutable
${else}
    node
${end @end}

${if model.type == 'modify' then 
    'mutable' 
  elseif model.type == 'query' then
    'immutable'
  else 'node'}

# invoke a function
${if_else_eq(model.type, 'modify', 'mutable', 'query', 'immutable', 'node')}  
```

### module statement

```
${import "/libs/mylib.rsl" as my}

${import "/libs/mylib.rsl"}

${include "/libs/mylib.rsl"}
```

### function/macro statement

```
${function avg x y}
    ${return (x+y) / 2}
${end}

${function avg nums...}
    ${assign sum = 0}
    ${for num in nums}
        ${assign sum = sum + num}
    ${end}
    ${return sum / nums.size}
${end}

${avg(10, 20, 30)}
```

```
${macro dup x y z}
    some text $x $y ${x + y + z}
    ${nested}
${end}

${#dup#end}

${#dup}
${#end}
```