# 🏢 HR Payroll System

A full-stack **HR & Payroll Management System** built with **Spring Boot** (backend) and **Next.js** (frontend), following a modular monolithic architecture with feature-based packaging.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [System Architecture](#system-architecture)
- [Features](#features)
- [Project Structure](#project-structure)
- [Database Design](#database-design)
- [API Endpoints](#api-endpoints)
- [Business Rules](#business-rules)
- [Getting Started](#getting-started)
- [Environment Variables](#environment-variables)
- [Onboarding Flow](#onboarding-flow)
- [Payroll Formula](#payroll-formula)
- [Role & Access Control](#role--access-control)
- [Screenshots](#screenshots)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

The HR Payroll System allows organizations to manage their workforce digitally. Admins can register employees, track attendance, and generate monthly payroll automatically based on hours worked and overtime. Employees can check in/out daily and view their own pay slips.

### Key Highlights

- 🔐 JWT-based authentication with role-based access control (ADMIN / EMPLOYEE)
- 👤 Separate `User` (login) and `Employee` (HR profile) entities for clean separation of concerns
- ⏱️ Automatic overtime calculation on check-out
- 💰 Automated payroll generation with BigDecimal precision arithmetic
- 🛡️ Employees can only access their own data — enforced via JWT, never via request params
- 🗂️ Feature-based packaging for both backend and frontend

---

## Tech Stack

### Backend
| Technology | Purpose |
|---|---|
| Java 17 + | Programming language |
| Spring Boot 4.0.1 | Application framework |
| Spring Security + JWT | Authentication & authorization |
| Spring Data JPA / Hibernate | ORM & database access |
| PostgreSQL | Primary database |
| Lombok | Boilerplate reduction |
| Maven | Build tool |

### Frontend
| Technology | Purpose |
|---|---|
| Next.js 14 (App Router) | React framework |
| TypeScript | Type safety |
| Redux Toolkit (RTK) | Global state management |
| RTK Query | API data fetching & caching |
| Tailwind CSS | Styling |

---

## System Architecture

```
┌─────────────────────────────────────────────────────┐
│                    Frontend (Next.js)                │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │
│  │  Auth    │  │ Employee │  │ Attendance/Payroll│  │
│  │  Feature │  │ Feature  │  │ Features          │  │
│  └──────────┘  └──────────┘  └──────────────────┘  │
│                RTK Query (API Layer)                 │
└───────────────────────┬─────────────────────────────┘
                        │ HTTP / REST (JSON)
                        │ Bearer Token (JWT)
┌───────────────────────▼─────────────────────────────┐
│                  Backend (Spring Boot)               │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │
│  │  Auth    │  │ Employee │  │ Attendance/Payroll│  │
│  │  Feature │  │ Feature  │  │ Features          │  │
│  └──────────┘  └──────────┘  └──────────────────┘  │
│              Spring Security (JWT Filter)            │
└───────────────────────┬─────────────────────────────┘
                        │ JPA / Hibernate
┌───────────────────────▼─────────────────────────────┐
│                   PostgreSQL Database                │
│   users │ employees │ attendances │ payrolls         │
└─────────────────────────────────────────────────────┘
```

---

## Features

### Admin Features
- ✅ Register user accounts with roles (ADMIN / EMPLOYEE)
- ✅ Create and update employee HR profiles
- ✅ View all employees
- ✅ View all attendance records across all employees
- ✅ Generate monthly payroll for one or all active employees
- ✅ Mark payroll records as PAID

### Employee Features
- ✅ Login with username and password
- ✅ View own HR profile
- ✅ Check in and check out daily (once per day enforced)
- ✅ View own attendance history
- ✅ View own payroll records and salary breakdown

---

## Project Structure

### Backend
```
src/main/java/com/thong/
├── feature/
│   ├── auth/
│   │   ├── AuthController.java
│   │   ├── AuthService.java
│   │   ├── AuthServiceImpl.java
│   │   ├── entity/
│   │   │   ├── User.java
│   │   │   └── Role.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   └── RoleRepository.java
│   │   └── dto/
│   │       ├── LoginRequest.java
│   │       ├── RegisterRequest.java
│   │       └── AuthResponse.java
│   │
│   ├── employee/
│   │   ├── Employee.java
│   │   ├── EmployeeController.java
│   │   ├── EmployeeService.java
│   │   ├── EmployeeServiceImpl.java
│   │   ├── EmployeeRepository.java
│   │   ├── EmployeeMapper.java
│   │   └── dto/
│   │       ├── CreateEmployeeRequest.java
│   │       ├── UpdateEmployeeRequest.java
│   │       └── EmployeeResponse.java
│   │
│   ├── attendance/
│   │   ├── Attendance.java
│   │   ├── AttendanceController.java
│   │   ├── AttendanceService.java
│   │   ├── AttendanceServiceImpl.java
│   │   ├── AttendanceRepository.java
│   │   ├── AttendanceMapper.java
│   │   └── dto/
│   │       ├── CheckInRequest.java
│   │       └── AttendanceResponse.java
│   │
│   └── payroll/
│       ├── Payroll.java
│       ├── PayrollController.java
│       ├── PayrollService.java
│       ├── PayrollServiceImpl.java
│       ├── PayrollRepository.java
│       ├── PayrollMapper.java
│       └── dto/
│           ├── GeneratePayrollRequest.java
│           └── PayrollResponse.java
│
├── exception/
│   └── GlobalExceptionHandler.java
│
└── config/
    ├── SecurityConfig.java
    └── JwtConfig.java
```

### Frontend
```
src/
├── app/
│   ├── (auth)/
│   │   └── login/
│   │       └── page.tsx
│   ├── (dashboard)/
│   │   ├── layout.tsx
│   │   ├── dashboard/page.tsx
│   │   ├── users/page.tsx
│   │   ├── employees/page.tsx
│   │   ├── attendance/page.tsx
│   │   └── payroll/page.tsx
│   └── layout.tsx
├── feature/
│   ├── auth/
│   │   ├── authSlice.ts
│   │   └── authApi.ts
│   ├── user/
│   │   └── userApi.ts
│   ├── employee/
│   │   └── employeeApi.ts
│   ├── attendance/
│   │   └── attendanceApi.ts
│   └── payroll/
│       └── payrollApi.ts
├── store.ts
├── provider.tsx
└── hooks.ts
```

---

## Database Design

### Entity Relationships

```
User  ──────────────── Employee
      (OneToOne)          │
                          │ OneToMany
                     ┌────┴────┐
                     │         │
                Attendance   Payroll
           (ManyToOne)   (ManyToOne)
```

### Tables

```sql
-- User login accounts
CREATE TABLE users (
    id       SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email    VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    dob      DATE,
    gender   VARCHAR(10)
);

-- Employee HR profiles
CREATE TABLE employees (
    id          SERIAL PRIMARY KEY,
    user_id     INTEGER UNIQUE NOT NULL REFERENCES users(id),
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    department  VARCHAR(100),
    position    VARCHAR(100),
    base_salary NUMERIC(15,2) NOT NULL,
    hire_date   DATE,
    is_active   BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP
);

-- Daily attendance records
CREATE TABLE attendances (
    id              SERIAL PRIMARY KEY,
    employee_id     INTEGER NOT NULL REFERENCES employees(id),
    date            DATE NOT NULL,
    check_in_time   TIMESTAMP,
    check_out_time  TIMESTAMP,
    total_hours     DOUBLE PRECISION,
    overtime_hours  DOUBLE PRECISION,
    UNIQUE (employee_id, date)
);

-- Monthly payroll records
CREATE TABLE payrolls (
    id            SERIAL PRIMARY KEY,
    employee_id   INTEGER NOT NULL REFERENCES employees(id),
    month         VARCHAR(7) NOT NULL,   -- format: yyyy-MM
    base_salary   NUMERIC(15,2),
    overtime_pay  NUMERIC(15,2),
    tax           NUMERIC(15,2),
    net_salary    NUMERIC(15,2),
    status        VARCHAR(20) DEFAULT 'GENERATED',
    UNIQUE (employee_id, month)
);
```

---

## API Endpoints

> Base URL: `http://localhost:8080/api/v1`
> All protected endpoints require: `Authorization: Bearer <token>`

### Auth

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/v1/auth/login` | Public | Login, returns JWT token |
| `POST` | `/v1/auth/register` | Public / Admin | Register new user account |

**Login Request:**
```json
{
  "username": "john",
  "password": "secret"
}
```

**Login Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "role": "EMPLOYEE"
}
```

**Register Request:**
```json
{
  "username": "john",
  "email": "john@company.com",
  "password": "secret123",
  "dob": "1995-04-20",
  "gender": "MALE",
  "roles": ["EMPLOYEE"]
}
```

---

### Employee

| Method | Endpoint | Role | Description |
|---|---|---|---|
| `POST` | `/v1/employees` | ADMIN | Create employee profile |
| `PUT` | `/v1/employees/{id}` | ADMIN | Update employee |
| `GET` | `/v1/employees` | ADMIN | List all employees |
| `GET` | `/v1/employees/{id}` | ADMIN | Get employee by ID |
| `GET` | `/v1/employees/me` | ADMIN / EMPLOYEE | Get own profile (JWT) |

**Create Employee Request:**
```json
{
  "userId": 5,
  "firstName": "John",
  "lastName": "Doe",
  "department": "Engineering",
  "position": "Backend Developer",
  "baseSalary": 3000.00,
  "hireDate": "2024-01-15"
}
```

**Employee Response:**
```json
{
  "id": 1,
  "userId": 5,
  "username": "john",
  "email": "john@company.com",
  "firstName": "John",
  "lastName": "Doe",
  "department": "Engineering",
  "position": "Backend Developer",
  "baseSalary": 3000.00,
  "hireDate": "2024-01-15",
  "isActive": true,
  "createdAt": "2024-01-15T09:00:00"
}
```

---

### Attendance

| Method | Endpoint | Role | Description |
|---|---|---|---|
| `POST` | `/v1/attendance/check-in` | EMPLOYEE | Check in (once per day) |
| `POST` | `/v1/attendance/check-out` | EMPLOYEE | Check out + auto-calculate hours |
| `GET` | `/v1/attendance/my` | ADMIN / EMPLOYEE | Own attendance history |
| `GET` | `/v1/attendance` | ADMIN | All employees' attendance |

**Check-In Request:**
```json
{
  "date": null
}
```
> `date: null` defaults to today. Pass `"2024-06-15"` to specify a date.

**Attendance Response:**
```json
{
  "id": 10,
  "employeeId": 1,
  "employeeName": "John Doe",
  "date": "2024-06-15",
  "checkInTime": "2024-06-15T09:00:00",
  "checkOutTime": "2024-06-15T18:30:00",
  "totalHours": 9.5,
  "overtimeHours": 1.5
}
```

---

### Payroll

| Method | Endpoint | Role | Description |
|---|---|---|---|
| `POST` | `/v1/payrolls/generate` | ADMIN | Generate payroll for a month |
| `GET` | `/v1/payrolls?month=yyyy-MM` | ADMIN | All payrolls (filter by month) |
| `PATCH` | `/v1/payrolls/{id}/pay` | ADMIN | Mark payroll as PAID |
| `GET` | `/v1/payrolls/my` | ADMIN / EMPLOYEE | Own payroll records |

**Generate Payroll Request:**
```json
{
  "employeeId": null,
  "month": "2024-06"
}
```
> `employeeId: null` generates for **all active employees**. Pass an ID for a single employee.

**Payroll Response:**
```json
{
  "id": 3,
  "employeeId": 1,
  "employeeName": "John Doe",
  "month": "2024-06",
  "baseSalary": 3000.00,
  "overtimePay": 562.50,
  "tax": 356.25,
  "netSalary": 3206.25,
  "status": "GENERATED"
}
```

---

## Business Rules

### Attendance Rules
- An employee can only check in **once per day** — enforced at both application and database level (`UNIQUE` constraint)
- Check-out must happen on the **same day** as check-in
- `totalHours` = `(checkOutTime - checkInTime)` in hours
- `overtimeHours` = `max(0, totalHours - 8)` — overtime only applies beyond 8 hours

### Payroll Rules
- Payroll can only be generated **once per employee per month** — enforced by unique constraint
- `hourlyRate` = `baseSalary / 160` (160 = standard monthly hours)
- `overtimePay` = `overtimeHours × hourlyRate × 1.5`
- `tax` = `(baseSalary + overtimePay) × 10%`
- `netSalary` = `baseSalary + overtimePay - tax`
- All calculations use `BigDecimal` with `RoundingMode.HALF_UP`
- `baseSalary` is stored as a **snapshot** — historical records stay accurate if salary changes

### Security Rules
- Employee ID is **always resolved from JWT** — never accepted from URL or request body
- Employees can only access their own profile, attendance, and payroll
- `@PreAuthorize` annotations enforce role checks before any method executes

---

## Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL 14+
- Maven 3.8+

### Backend Setup

**1. Clone the repository**
```bash
git clone https://github.com/ButSeavThong/hr-payroll-backend.git
cd hr-payroll-system/backend
```

**2. Configure the database**

Create a PostgreSQL database:
```sql
CREATE DATABASE hr_payroll_db;
```

Update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/hr_payroll_db
    username: your_db_username
    password: your_db_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

app:
  jwt:
    secret: your_jwt_secret_key_min_32_characters
    expiration: 86400000   # 24 hours in milliseconds
```

**3. Run the backend**
```bash
mvn spring-boot:run
```

Backend starts at: `http://localhost:8080`

---

### Frontend Setup

**1. Navigate to frontend directory**
```bash
cd ../frontend
```

**2. Install dependencies**
```bash
npm install
```

**3. Configure environment**

Create `.env.local`:
```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api/v1
```

**4. Run the frontend**
```bash
npm run dev
```

Frontend starts at: `http://localhost:3000`

---

## Environment Variables

### Backend (`application.yml`)

| Variable | Description | Example |
|---|---|---|
| `spring.datasource.url` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/hr_payroll_db` |
| `spring.datasource.username` | Database username | `postgres` |
| `spring.datasource.password` | Database password | `yourpassword` |
| `app.jwt.secret` | JWT signing secret (min 32 chars) | `mySecretKey1234567890abcdefghijk` |
| `app.jwt.expiration` | Token expiry in milliseconds | `86400000` |

### Frontend (`.env.local`)

| Variable | Description | Example |
|---|---|---|
| `NEXT_PUBLIC_API_BASE_URL` | Backend API base URL | `http://localhost:8080/api/v1` |

---

## Onboarding Flow

> This is the correct order to onboard a new employee into the system.

```
Step 1 ── Admin creates a User account
          POST /api/v1/auth/register
          { username, email, password, roles: ["EMPLOYEE"] }
          → Note the returned userId (e.g., 5)

Step 2 ── Admin creates the Employee profile using that userId
          POST /api/v1/employees
          { userId: 5, firstName, lastName, baseSalary, ... }
          → Employee profile is now linked to the login account

Step 3 ── Employee logs in
          POST /api/v1/auth/login
          { username, password }
          → Receives JWT token

Step 4 ── Employee checks in daily
          POST /api/v1/attendance/check-in
          → Check-out at end of day: POST /api/v1/attendance/check-out

Step 5 ── Admin generates payroll at month end
          POST /api/v1/payrolls/generate
          { month: "2024-06" }

Step 6 ── Admin marks payroll as PAID after salary transfer
          PATCH /api/v1/payrolls/{id}/pay
```

---

## Payroll Formula

```
hourlyRate   = baseSalary ÷ 160
overtimePay  = overtimeHours × hourlyRate × 1.5
gross        = baseSalary + overtimePay
tax          = gross × 10%
netSalary    = gross − tax
```

### Example

> Employee base salary: **$3,000/month**, worked **20 overtime hours** this month.

| Step | Calculation | Result |
|---|---|---|
| Hourly Rate | $3,000 ÷ 160 | $18.75 / hr |
| Overtime Pay | 20h × $18.75 × 1.5 | $562.50 |
| Gross | $3,000 + $562.50 | $3,562.50 |
| Tax (10%) | $3,562.50 × 10% | $356.25 |
| **Net Salary** | $3,562.50 − $356.25 | **$3,206.25** |

---

## Role & Access Control

| Action | ADMIN | EMPLOYEE |
|---|---|---|
| Register User | ✅ | ❌ |
| Create / Update Employee | ✅ | ❌ |
| View all employees | ✅ | ❌ |
| View own profile | ✅ | ✅ |
| Check in / Check out | ❌ | ✅ |
| View own attendance | ✅ | ✅ |
| View all attendance | ✅ | ❌ |
| Generate payroll | ✅ | ❌ |
| Mark payroll as PAID | ✅ | ❌ |
| View own payroll | ✅ | ✅ |

> 🔒 **Security note:** For any endpoint returning personal data, the employee identity is always resolved from the JWT token — never from a URL parameter or request body. This prevents employees from accessing other employees' data.

---

## Screenshots

> _Add screenshots here after UI is built._

| Page | Description |
|---|---|
| `/login` | Login form |
| `/dashboard` | Summary cards — employees, attendance today, payroll status |
| `/users` | Admin: manage user accounts |
| `/employees` | Admin: manage employee HR profiles |
| `/attendance` | Admin: all attendance / Employee: own history + check-in/out |
| `/payroll` | Admin: generate & manage / Employee: view own pay slips |

---

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m "feat: add your feature"`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Open a Pull Request

### Commit Message Convention

```
feat:     new feature
fix:      bug fix
refactor: code refactor without feature change
docs:     documentation update
style:    formatting, missing semicolons, etc.
test:     adding or updating tests
chore:    build process or dependency update
```

---

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

<div align="center">
  Built with ❤️ using Spring Boot & Next.js
</div>
