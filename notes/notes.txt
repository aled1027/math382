lg = log base 2 of x

Selection Sort
	- Find smallest element, swap it with the first element
	- so you literally sort moving from left to right
	- only swap when placing an element
		- seems like it has fewer swaps than most other algorithms
MergeSort (A, n)
	Merge (L,R,B,n):
		# L[n/2] = infinitity (aka sentinel value)
		# Same with R
		# Invariant: Array is already split into to two arrays, L and R
		# Sentinel value is hack that allows us to avoid checking to see if we are finished with L or R
		i=0; j=0;
		for k=0 to n-1:
			if L[i] < r[j]:
				B[k] = L[i]; i++;
			else:
				B[k] = R[j]; j++;
	Merge-Sort(A,n):
		if n <= 1:
			return A
		else:
			(L,R) = Slice-In-Two(A, n) 
				# let L[0..n/2-1] = A[0..n/2-1]
				# let R[0..n/2-1] = A[n/2..n-1]
				# and set up sentinel values
				# to save memory, just focus in on different slices using indices
					# Don't just create new arrays
			Merge-Sort(L, n/2)
			Merge-Sort(R, n/2)
			Merge(L, R, A, n)
	Analysis
		K comparisons at each level => n = 2^k
		Total number of comparisons = n*k = n*lg(n)

