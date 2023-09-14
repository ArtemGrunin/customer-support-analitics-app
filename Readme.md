# Customer Support Analytics App

This is a solution for the test task provided in [this Google Drive link](https://drive.google.com/file/d/1EmHPTL99sJ_Drv1c07hb1QE9xTvjDlFt/view?usp=sharing).

## Problem Statement

A web hosting company provides customer support via email. They record reply waiting time, the type of question, category, and service. To improve customer satisfaction, they need an analytical tool to evaluate this data.

### Input

- The company provides 10 different services, each with 3 variations.
- Questions are divided into 10 types, each can belong to 20 categories, and a category can have 5 subcategories.
- The input begins with a line containing count `S` (<= 100,000) of all lines.
- Each line starts with character `C` (waiting timeline) or `D` (query).

#### Waiting Timeline:
- `C service_id[.variation_id] question_type_id[.category_id.[sub-category_id]] P/N date time`
- Values in square brackets are optional.
- `service_id[.variation_id]` - Service 9.1 represents service 9 of variation 1.
- `question_type_id[.category_id.[sub-category_id]]` - Question type 7.14.4 represents question type 7, category 14, and sub-category 4.
- `P/N` - Response type ‘P’ (first answer) or ‘N’ (next answer).
- `date` - Response date format is DD.MM.YYYY, e.g., 27.11.2012 (27 November 2012).
- `time` - Time in minutes represents waiting time.

#### Query Line:
- `D service_id[.variation_id] question_type_id[.category_id.[sub-category_id]] P/N date_from[-date_to]`
- Represents a query that prints out the average waiting time of all records matching specific criteria.
- `service_id` and `question_type_id` can have a special value "*," meaning the query matches all services/question types. In this case, neither service variation nor service category/subcategory can be specified.

### Output

- Query line of type ‘D’ prints out the average waiting time rounded to minutes.
- Only matching lines defined before the query line are counted.
- It prints out "-" if the output is not defined.

### Example

**Input:**
7<br>
C 1.1 8.15.1 P 15.10.2012 83<br>
C 1 10.1 P 01.12.2012 65<br>
C 1.1 5.5.1 P 01.11.2012 117<br>
D 1.1 8 P 01.01.2012-01.12.2012<br>
C 3 10.2 N 02.10.2012 100<br>
D 1 * P 8.10.2012-20.11.2012<br>
D 3 10 P 01.12.2012<br>

**Output:**
<br>83<br>
100<br>
-<br>

### How to run

✅Clone the project from GitHub<br>
✅Put your test text into `src/main/resourses/input_data.txt` (if you want)<br>
✅Run and enjoy the program

### Author

Artem Grunin