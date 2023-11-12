import pandas as pd
from scipy import stats
import matplotlib.pyplot as plt
import numpy as np


# This data is for the time complexity. Column 1 to 3 are the time spent to startup the game for each grid size for code version 1, 2, and 3.
# code version 1 is the original code.
# code version 2 is the refactored.
# code version 3 is the refactored along with splitting the class into three separated classes.
data = {
    "Grid Size": [10, 20, 30, 40, 50, 60, 70, 80, 90, 100],
    "Column1": [173, 156, 176, 245, 354, 568, 841, 1316, 1611, 2406],
    "Column2": [258, 133, 153, 181, 200, 231, 290, 286, 346, 456],
    "Column3": [1263, 1451, 1876, 2569, 2447, 3316, 2914, 3733, 4101, 4698]
}

# This data is for the space complexity. Column 1 to 3 are the memory spent to startup the game for each grid size for code version 1, 2, and 3.
new_data = {
    "Grid Size": [10, 20, 30, 40, 50, 60, 70, 80, 90, 100],
    "Code 1 Memory (bytes)": [1576676, 4021598, 8653293, 17744654, 21820005, 40758936, 63699699, 128204491, 201587040, 652940115],
    "Code 2 Memory (bytes)": [3872440, 3574340, 3614076, 5042543, 3559532, 3600794, 6250956, 3711813, 4255257, 6710794],
    "Code 3 Memory Used (bytes)": [7142192, 27821792, 67912832, 36416880, 48120640, 23166488, 94135128, 92122256, 110093144, 123369472]
}

# dataframes
df = pd.DataFrame(data)
new_df = pd.DataFrame(new_data)

# Calculating the mean of each column for time
mean_values = df.mean()
# Calculating the mean of each code's memory usage
mean_values_new = new_df.mean()

mean_values
mean_values_new


# Performing t-tests for time
# t-test between columns 1 and 2
t_test_1_2 = stats.ttest_rel(df['Column1'], df['Column2'])
# t-test between columns 1 and 3
t_test_1_3 = stats.ttest_rel(df['Column1'], df['Column3'])


# Performing t-tests for memory
t_test_code1_code2 = stats.ttest_rel(new_df['Code 1 Memory (bytes)'], new_df['Code 2 Memory (bytes)'])
t_test_code1_code3 = stats.ttest_rel(new_df['Code 1 Memory (bytes)'], new_df['Code 3 Memory Used (bytes)'])


t_test_1_2, t_test_1_3
t_test_code1_code2, t_test_code1_code3, 



# Data for Test 1, Test 2, and Test 3. Times in milliseconds
times_test1 = [173, 156, 176, 245, 354, 568, 841, 1316, 1611, 2406]
times_test2 = [258, 133, 153, 181, 200, 231, 290, 286, 346, 456]
times_test3 = [1263, 1451, 1876, 2569, 2447, 3316, 2914, 3733, 4101, 4698]

# Memories in bytes
memories_test1 = [1576676, 4021598, 8653293, 17744654, 21820005, 40758936, 63699699, 128204491, 201587040, 652940115]
memories_test2 = [3872440, 3574340, 3614076, 5042543, 3559532, 3600794, 6250956, 3711813, 4255257, 6710794]
memories_test3 = [7142192, 27821792, 67912832, 36416880, 48120640, 23166488, 94135128, 92122256, 110093144, 123369472]


# Generating boxplot for times
plt.figure(figsize=(8, 6))
plt.boxplot([times_test1, times_test2, times_test3], labels=['Test 1', 'Test 2', 'Test 3'])
plt.title('Boxplot of Times')
plt.ylabel('Time (ms)')
plt.savefig('boxplot_times.png')
plt.close()

# Generating boxplot for memories separately
plt.figure(figsize=(8, 6))
plt.boxplot([memories_test1, memories_test2, memories_test3], labels=['Test 1', 'Test 2', 'Test 3'])
plt.title('Boxplot of Memories')
plt.ylabel('Memory (bytes)')
plt.savefig('boxplot_memories.png')
plt.close()




# Preparing the grid size data
grid_sizes = [10, 20, 30, 40, 50, 60, 70, 80, 90, 100]

# Creating linear plots for time vs grid size
plt.figure(figsize=(10, 6))
plt.plot(grid_sizes, times_test1, marker='o', label='Test 1 Time')
plt.plot(grid_sizes, times_test2, marker='x', label='Test 2 Time')
plt.plot(grid_sizes, times_test3, marker='^', label='Test 3 Time')
plt.title('Time vs Grid Size')
plt.xlabel('Grid Size')
plt.ylabel('Time (ms)')
plt.legend()
plt.grid(True)
plt.savefig('time_vs_grid_size.png')
plt.close()

# Creating linear plots for memory vs grid size
plt.figure(figsize=(10, 6))
plt.plot(grid_sizes, memories_test1, marker='o', label='Test 1 Memory')
plt.plot(grid_sizes, memories_test2, marker='x', label='Test 2 Memory')
plt.plot(grid_sizes, memories_test3, marker='^', label='Test 3 Memory Used')
plt.title('Memory vs Grid Size')
plt.xlabel('Grid Size')
plt.ylabel('Memory (bytes)')
plt.legend()
plt.grid(True)
plt.savefig('memory_vs_grid_size.png')
plt.close()
