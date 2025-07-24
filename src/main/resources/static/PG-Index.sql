
CREATE INDEX idx_cart_item_cart_id ON cart_item(cart_id);
CREATE INDEX idx_cart_item_product_id ON cart_item(product_id);
CREATE UNIQUE INDEX idx_cart_item_cart_product ON cart_item(cart_id, product_id);