# RestaurantManagementSystem

Every good restaurant has an ordering system. It serves not only to automate the work of the waiters, but also of the kitchen. The waiter should be able to create an order for a certain table, and the kitchen should change its status to "cooking", "prepared", respectively the waiter should be able to change it to "served". The waiter can remove the bill for the given table, which changes the status to "paid" and frees the table. Each order consists of dishes and drinks from the menu. The waiter can enter and change the menu with names, prices and type.

The project must be able to:
There should be two roles: waiter and cook. They are logged in at the beginning of using the program.
The cook can only see the new orders, change their status to "cooking" and "prepared", as well as make a reference (described below)
The waiter can change the menu - add, change or remove dishes. Each dish has a name, price and type.
The waiter can see the menu in a separate page. In it, the dishes are divided by type.
The waiter can see a list of all active orders.
The waiter can create and edit orders. Each order has a date and time of creation and a table number. No more than one table order can be created at a time.
Dishes can be added or removed from each order. Each dish can be added once or many times. The total price of the order is displayed in real time.
The waiter can change the order status to "served".
The waiter can change the order status to "paid". Then he is shown a summary of the order, it disappears from the list of active orders, and a new order can now be placed on that table.
The program saves its data (dish menu, user data, orders) in a database. Allows to make inquiries
For waiters: List of tables served by the logged in waiter. With the ability to sort and filter by date.
For chefs: A list of order counts for a date, arranged chronologically starting with the most recent date. On each date there is a details button that opens a new page with a list of all orders for the day and information about them.
