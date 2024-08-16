INSERT INTO products(purchase_price, sale_price, barcode, description, image_url, name) VALUES(5.0, 6.5, '000001', 'Arrocete', 'http://image.com', 'Arroz de Valencia');
INSERT INTO products(purchase_price, sale_price, barcode, description, image_url, name) VALUES(2.2, 2.6, '000002', 'Galletas', 'http://image.com', 'Galletas sin gluten');
INSERT INTO products(purchase_price, sale_price, barcode, description, image_url, name) VALUES(1.0, 1.5, '000003', 'Leche', 'http://image.com', 'Leche de vaca');

INSERT INTO products_tags(product_entity_barcode, tags) VALUES('000001', 'arroz');
INSERT INTO products_tags(product_entity_barcode, tags) VALUES('000002', 'reposteria');
INSERT INTO products_tags(product_entity_barcode, tags) VALUES('000002', 'sin gluten');
INSERT INTO products_tags(product_entity_barcode, tags) VALUES('000003', 'lacteos');

INSERT INTO addresses(id, city, country_code, street, zip_code) VALUES(1, 'Valencia', 'ES', 'Calle de la Paz, 123', '46001');
INSERT INTO addresses(id, city, country_code, street, zip_code) VALUES(2, 'Valencia', 'ES', 'Carrer Andres Estelles, 34', '46002');
INSERT INTO addresses(id, city, country_code, street, zip_code) VALUES(3, 'Valencia', 'ES', 'Carrer Jaume I, 12', '46002');

INSERT INTO suppliers(billing_address_id, id, iban, name) VALUES(1, 1, 'ES6621000418401234567891', 'La Fallera');
INSERT INTO suppliers(billing_address_id, id, iban, name) VALUES(2, 2, 'ES6621000418401234567892', 'Arroces del Ebro');
INSERT INTO suppliers(billing_address_id, id, iban, name) VALUES(3, 3, 'ES6621000418401234567893', 'Leche y Repostería S.A.');
ALTER SEQUENCE address_id_seq RESTART WITH 4;

INSERT INTO products_suppliers(supplier_id, product_barcode) VALUES (1, '000001');
INSERT INTO products_suppliers(supplier_id, product_barcode) VALUES (2, '000001');
INSERT INTO products_suppliers(supplier_id, product_barcode) VALUES (3, '000002');
INSERT INTO products_suppliers(supplier_id, product_barcode) VALUES (3, '000003');
