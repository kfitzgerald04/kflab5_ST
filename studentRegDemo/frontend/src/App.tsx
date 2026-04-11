import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import StudentList from "./StudentList.tsx";
import CourseList from "./CourseList.tsx";

function App() {
  return (
    <BrowserRouter>
      <div style={{ padding: "20px", fontFamily: "Arial" }}>
        <nav style={{ marginBottom: "20px" }}>
          <Link id="nav-course-list-link" to="/" style={{ marginRight: "20px" }}>
            Course List
          </Link>
          <Link id="nav-student-list-link" to="/students">
            Student List
          </Link>
        </nav>

        <Routes>
          <Route path="/" element={<CourseList />} />
          <Route path="/students" element={<StudentList />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;