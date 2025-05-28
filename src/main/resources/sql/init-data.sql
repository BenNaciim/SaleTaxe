-- Insertion d'articles
INSERT INTO public.articles (id, name, is_imported, taxes_rate, quantity, pricebt)
VALUES
    (1, 'Book', false, 'Exempt', 10, 12.49),
    (2, 'Music CD', false, 'Goods', 5, 14.99),
    (3, 'Chocolate Bar', false, 'Exempt', 20, 0.85),
    (4, 'Imported Box of Chocolates', true, 'Exempt', 15, 10.00),
    (5, 'Imported Bottle of Perfume', true, 'Goods', 7, 47.50),
    (6, 'packet of headache pills', false, 'Exempt', 50, 9.75),
    (7, 'Bandages', false, 'Exempt', 30, 2.49),
    (8, 'Imported Vitamins', true, 'Exempt', 20, 15.00),
    (9, 'Apple', false, 'Exempt', 100, 0.50),
    (10, 'Bread', false, 'Exempt', 40, 1.20),
    (11, 'Imported Cheese', true, 'Exempt', 25, 8.75),
    (12, 'bottle of perfume', false, 'Goods', 7, 18.99),
    (13, 'Imported Bottle of Perfume 1 ', true, 'Goods', 7, 27.99),
    (14, 'Imported Box of Chocolates 1', true, 'Exempt', 7, 11.25) ;