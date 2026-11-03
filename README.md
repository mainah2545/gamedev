# gamedev

แอป Android (Kotlin + Android Views/XML) สำหรับงาน #5, #6 และ #7 โหลดรูปและข้อความจาก GitHub ผ่าน raw URL ของ repository นี้

## ที่มาของโค้ด

โปรเจกต์นี้เริ่มจากงานของ `winterzmr/gamedev` (https://github.com/winterzmr/gamedev) ซึ่งอาจารย์อนุญาตให้ใช้โค้ดร่วมกันได้ ประวัติ commit 3 รายการแรกเป็นของผู้เขียนต้นฉบับและเก็บไว้ตามเดิม ส่วน commit ล่าสุดเป็นการปรับลิงก์ให้ชี้มาที่ repository นี้

## โครงสร้าง

- `assets/assignment5/` รูป PNG 5 ไฟล์ของงาน #5 และรายการลิงก์ใน `submission/assignment5_urls.txt`
- `assets/assignment6/images/` และ `assets/assignment6/texts/` รูป 10 รูปและข้อความ 10 ไฟล์ของงาน #6
- `assets/assignment7/images/` รูป placeholder 10 รูปของงาน #7
- `app/` โค้ดแอป

## หน้าจอในแอป

- งาน #6: เมนูหลัก, รายการรูป, ดูรูป, รายการข้อความ, ดูข้อความ, สรุปการโหลด
- งาน #7: แกลเลอรีรูปภาพ (ViewPager2 ปัดซ้ายขวา วนลูป ตัวนับ auto slide ทุก 3 วินาที เสียง ปุ่มสุ่ม)

## การทำงานกับลิงก์

ฐานของลิงก์ที่แอปใช้อยู่ใน `app/src/main/java/com/kanyarat/gamedev/RemoteUrls.kt` (ค่า `ROOT`) ชี้ไปที่ `https://raw.githubusercontent.com/mainah2545/gamedev/main/assets`
