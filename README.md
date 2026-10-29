# inheritance-lab (Class 16)

A small lab to compare the three ways that JPA stores a family of classes. It is **not** part of ShopNest. Spring Boot 4.1.1 (made with Spring Initializr: Maven, Java 17, Spring Data JPA, MySQL Driver, Lombok), MySQL 8 or newer.

## The family

`User` (abstract: `id` as a version 7 UUID, `email`) with two children: `Customer` (`shippingAddress`) and `Seller` (`shopName`). A small `@Bean CommandLineRunner lab(UserRepo userRepo)` in `InheritancelabApplication` saves one customer and one seller, and then asks the **parent** repository for all users. (An earlier draft had a separate `LabRunner` class. It was removed, so the lab has fewer files to type.)

## How to run it

1. Keep your own `DB_PASSWORD`. The lab makes its own database `inheritance_lab` (`createDatabaseIfNotExist=true`) and uses `ddl-auto=update`. Use `ddl-auto=update` only in a throw-away lab.
2. In `User.java`, set the strategy in `@Inheritance(strategy = ...)`: `SINGLE_TABLE` (as it is now), `JOINED` or `TABLE_PER_CLASS`.
3. **Before every run**, in Workbench: `DROP DATABASE inheritance_lab;` (the tables of the earlier strategy would be in the way).
4. `mvn spring-boot:run`, and read the SQL in the console. The program ends by itself.

## What you should see (tested 29 Oct 2026)

| Strategy | Tables | Inserts for one customer | `findAll()` on the parent |
|---|---|---|---|
| `SINGLE_TABLE` | 1: `users` (`id`, `dtype`, `email`, `shipping_address`, `shop_name`) | 1 | one `select` from `users` |
| `JOINED` | 3: `users` (`id`, `email`), `customer` (`id`, `shipping_address`), `seller` (`id`, `shop_name`), with foreign keys from `customer.id` and `seller.id` to `users.id` | 2 (`users`, then `customer`) | two `left join`s and a `case when` |
| `TABLE_PER_CLASS` | 2: `customer` (`id`, `email`, `shipping_address`) and `seller` (`id`, `email`, `shop_name`) | 1 | a `union all` of both tables |

The last lines of every run print `Customer: asha@shopnest.com` and `Seller: ravi@shopnest.com`: the parent repository gives back objects of the **real** classes.

More things that were tested:
- `CustomerRepo.findAll()` (only one child): `SINGLE_TABLE` adds `where dtype = 'Customer'`, `JOINED` does one `join` of `customer` and `users`, and `TABLE_PER_CLASS` reads only `customer`.
- `TABLE_PER_CLASS` with an `IDENTITY` (counting) id does not start: `Cannot use identity column key generation with <union-subclass> mapping`. UUIDs are fine.
- With `create-drop` instead of `update`, the first `JOINED` run prints long (harmless) stack traces, because Hibernate tries to drop foreign keys of tables that do not exist yet. That is why the lab uses `update` and a fresh database for each run.
