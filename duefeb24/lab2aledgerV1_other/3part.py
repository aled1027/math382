def part():
	v1 = a[iv1]
	v2 = a[iv2]
	print v1, v2
	temp = a[iv1]; a[iv1] = a[r-1]; a[r-1] = temp; # swap iv1 and r-1
	temp = a[iv2]; a[iv2] = a[r]; a[r] = temp; # swap iv2 and r
	i = l
	j = l
	for k in range(l,r+1):
		if a[k] <= v2:
			if a[k] <= v1:
				temp = a[j]; a[j] = a[k]; a[k] = temp;
				temp = a[i]; a[i] = a[j]; a[j] = temp;
				i=i+1
				j=j+1
			else:
				temp = a[j]; a[j] = a[k]; a[k] = temp;
				j=j+1
	return i+1

a = range(0,10)
a.reverse()
iv1 = 7
iv2 = 2
l = 0
r =len(a)-1

print "before: ", a
part()
print a

