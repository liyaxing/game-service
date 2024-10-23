import csv
import random
import string
from datetime import datetime, timedelta

def generate_game_name():
    """
    Generates a random game name with a length between 1 and 20 characters.
    """
    length = random.randint(1, 20)
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

def generate_game_code():
    """
    Generates a random game code with a length between 1 and 5 characters.
    """
    length = random.randint(1, 5)
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

def generate_random_date(start_date, end_date):
    """
    Generates a random timestamp from the specified start_date up to and including end_date.

    Args:
        start_date (datetime): The start date for the range.
        end_date (datetime): The end date for the range.

    Returns:
        datetime: A randomly selected timestamp within the specified range.
    """
    delta = end_date - start_date
    random_seconds = random.randint(0, int(delta.total_seconds()))
    return start_date + timedelta(seconds=random_seconds)
    
def generate_csv(n=10):
    """
    Generates a CSV file with n rows of data for games.

    Args:
        n (int): The number of rows of data to generate. Default is 10.
    """
    filename = "large_test.csv"
    start_date = datetime(2024, 4, 1)
    end_date = datetime(2024, 4, 30)
    
    with open(filename, mode='w', newline='') as file:
        writer = csv.writer(file)

        writer.writerow(["id", "game_no", "game_name", "game_code", "type", "cost_price", "tax", "sale_price", "date_of_sale"])
        
        for i in range(1, n + 1):
            id_field = i
            game_no = random.randint(1, 100)
            game_name = generate_game_name()
            game_code = generate_game_code()
            type_field = random.choice([1, 2])
            cost_price = round(random.uniform(1, 100), 2)
            tax = 0.09
            sale_price = round(cost_price * (1 + tax), 2)
            date_of_sale = generate_random_date(start_date, end_date).strftime("%Y-%m-%d %H:%M:%S")
            
            writer.writerow([id_field, game_no, game_name, game_code, type_field, cost_price, tax, sale_price, date_of_sale])
    
    print(f"{filename} has been created successfully with {n} rows.")

if __name__ == "__main__":
    user_input = input("Enter the number of rows (press Enter to use default): ")
    n = int(user_input) if user_input else 10
    generate_csv(n)
