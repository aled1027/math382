def insertionSort(arr):
	for i in range(1,len(arr)):
		j = i
		while arr[j-1]>arr[j] and j>0:
			# swap:
			temp = arr[j-1]; arr[j-1] = arr[j]; arr[j] = temp;
			# increment
			j=j-1
	return arr

arr = [2,57,98,5,18,6]
print arr
print insertionSort(arr)
print iSort(arr)
