from itertools import permutations

def generate_schedules_non_interleaved(transactions):
    f = open("outputSchedules.txt", "w")
    
    perm = permutations(transactions)
    for p in perm:
        schedule = []
        for part in p:
            schedule += part
        
        print(str(schedule))
        f.write(str(schedule)+"\n")

    f.close()
    return None


transactions = [
    ['T1_R(X)', 'T1_W(X)', 'T1_R(Y)', 'T1_W(Y)'],
    ['T2_R(Z)', 'T2_R(Y)', 'T2_W(Y)', 'T2_R(X)', 'T2_W(X)'],
    ['T3_R(Y)', 'T3_R(Z)', 'T3_W(Y)', 'T3_W(Z)']
]


#Print all the Schedules
print("All possible schedules with no transactions interleaved:")
schedules = generate_schedules_non_interleaved(transactions)
