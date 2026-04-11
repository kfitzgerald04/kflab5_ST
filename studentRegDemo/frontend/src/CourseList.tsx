import { useEffect, useState } from "react";

type Student = {
  id: number;
  name: string;
  major: string;
  gpa: number;
};

type Course = {
  id: number;
  name: string;
  size: number;
  room: string;
  instructor: string;
  roster: Student[];
};

const COURSE_API = "http://localhost:8080/api/courses";
const STUDENT_API = "http://localhost:8080/api/students";

function CourseList() {
  const [courses, setCourses] = useState<Course[]>([]);
  const [students, setStudents] = useState<Student[]>([]);

  const [newName, setNewName] = useState("");
  const [newInstructor, setNewInstructor] = useState("");
  const [newMaxSize, setNewMaxSize] = useState("");
  const [newRoom, setNewRoom] = useState("");

  const [editingId, setEditingId] = useState<number | null>(null);
  const [editName, setEditName] = useState("");
  const [editInstructor, setEditInstructor] = useState("");
  const [editMaxSize, setEditMaxSize] = useState("");
  const [editRoom, setEditRoom] = useState("");

  const [selectedStudent, setSelectedStudent] = useState("");
  const [removeStudentId, setRemoveStudentId] = useState("");

  const loadCourses = async () => {
    const res = await fetch(COURSE_API);
    const data = await res.json();
    setCourses(data);
  };

  const loadStudents = async () => {
    const res = await fetch(STUDENT_API);
    const data = await res.json();
    setStudents(data);
  };

  useEffect(() => {
    loadCourses();
    loadStudents();
  }, []);

  const createCourse = async () => {
    await fetch(COURSE_API, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name: newName,
        instructor: newInstructor,
        size: Number(newMaxSize),
        room: newRoom,
      }),
    });

    setNewName("");
    setNewInstructor("");
    setNewMaxSize("");
    setNewRoom("");
    loadCourses();
  };

  const deleteCourse = async (id: number) => {
    await fetch(`${COURSE_API}/${id}`, {
      method: "DELETE",
    });
    loadCourses();
  };

  const startEdit = (course: Course) => {
    setEditingId(course.id);
    setEditName(course.name);
    setEditInstructor(course.instructor);
    setEditMaxSize(course.size.toString());
    setEditRoom(course.room);
    setSelectedStudent("");
    setRemoveStudentId("");
  };

  const saveEdit = async () => {
    if (editingId === null) return;

    await fetch(`${COURSE_API}/${editingId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name: editName,
        instructor: editInstructor,
        size: Number(editMaxSize),
        room: editRoom,
      }),
    });

    setEditingId(null);
    loadCourses();
  };

  const addStudentToCourse = async () => {
    if (editingId === null || !selectedStudent) return;

    await fetch(`${COURSE_API}/${editingId}/students/${selectedStudent}`, {
      method: "POST",
    });

    loadCourses();
    setSelectedStudent("");
  };

  const removeStudentFromCourse = async () => {
    if (editingId === null || !removeStudentId) return;

    await fetch(`${COURSE_API}/${editingId}/students/${removeStudentId}`, {
      method: "DELETE",
    });

    loadCourses();
    setRemoveStudentId("");
  };

  return (
    <div>
      <h2>Course List</h2>

      <div id="new-course-fields" style={{ marginBottom: "20px" }}>
        <input
          id="new-course-name"
          placeholder="Course Name"
          value={newName}
          onChange={(e) => setNewName(e.target.value)}
        />
        <input
          id="new-course-instructor"
          placeholder="Instructor"
          value={newInstructor}
          onChange={(e) => setNewInstructor(e.target.value)}
        />
        <input
          id="new-course-max-size"
          placeholder="Max Size"
          value={newMaxSize}
          onChange={(e) => setNewMaxSize(e.target.value)}
        />
        <input
          id="new-course-room"
          placeholder="Room"
          value={newRoom}
          onChange={(e) => setNewRoom(e.target.value)}
        />
        <button onClick={createCourse}>Create Course</button>
      </div>

      <table id="course-list-table" border={1} cellPadding={8}>
        <thead>
          <tr>
            <th>Name</th>
            <th>Instructor</th>
            <th>Max Size</th>
            <th>Room</th>
            <th>Roster</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {courses.map((course) => (
            <tr id={`course-row-${course.id}`} key={course.id}>
              <td id={`course-name-${course.id}`}>{course.name}</td>
              <td id={`course-instructor-${course.id}`}>{course.instructor}</td>
              <td id={`course-max-size-${course.id}`}>{course.size}</td>
              <td id={`course-room-${course.id}`}>{course.room}</td>
              <td id={`course-roster-${course.id}`}>
                {course.roster.map((s) => s.name).join(", ")}
              </td>
              <td>
                <button id="edit-course-button" onClick={() => startEdit(course)}>
                  Edit
                </button>
                <button id="delete-course-button" onClick={() => deleteCourse(course.id)}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {editingId !== null && (
        <div style={{ marginTop: "20px" }}>
          <h3>Edit Course</h3>

          <input
            id="edit-course-name"
            value={editName}
            onChange={(e) => setEditName(e.target.value)}
          />
          <input
            id="edit-course-instructor"
            value={editInstructor}
            onChange={(e) => setEditInstructor(e.target.value)}
          />
          <input
            id="edit-course-max-size"
            value={editMaxSize}
            onChange={(e) => setEditMaxSize(e.target.value)}
          />
          <input
            id="edit-course-room"
            value={editRoom}
            onChange={(e) => setEditRoom(e.target.value)}
          />

          <div style={{ marginTop: "10px" }}>
            <select
              id="select-student"
              value={selectedStudent}
              onChange={(e) => setSelectedStudent(e.target.value)}
            >
              <option value="">Select student</option>
              {students.map((student) => (
                <option key={student.id} value={student.id}>
                  {student.name}
                </option>
              ))}
            </select>
            <button id="add-student-button" onClick={addStudentToCourse}>
              Add Student
            </button>
          </div>

          <div style={{ marginTop: "10px" }}>
            <select
              id="remove-student-select"
              value={removeStudentId}
              onChange={(e) => setRemoveStudentId(e.target.value)}
            >
              <option value="">Remove student</option>
              {courses
                .find((c) => c.id === editingId)
                ?.roster.map((student) => (
                  <option key={student.id} value={student.id}>
                    {student.name}
                  </option>
                ))}
            </select>
            <button id="remove-student-button" onClick={removeStudentFromCourse}>
              Remove Student
            </button>
          </div>

          <div style={{ marginTop: "10px" }}>
            <button id="edit-course-save-button" onClick={saveEdit}>
              Save
            </button>
            <button id="edit-course-cancel-button" onClick={() => setEditingId(null)}>
              Cancel
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default CourseList;