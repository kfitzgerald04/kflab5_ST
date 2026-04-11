import { useEffect, useState } from "react";

type Student = {
  id: number;
  name: string;
  major: string;
  gpa: number;
};

const API = "http://localhost:8080/api/students";

function StudentList() {
  const [students, setStudents] = useState<Student[]>([]);
  const [newName, setNewName] = useState("");
  const [newMajor, setNewMajor] = useState("");
  const [newGpa, setNewGpa] = useState("");

  const [editingId, setEditingId] = useState<number | null>(null);
  const [editName, setEditName] = useState("");
  const [editMajor, setEditMajor] = useState("");
  const [editGpa, setEditGpa] = useState("");

  const loadStudents = async () => {
    const res = await fetch(API);
    const data = await res.json();
    setStudents(data);
  };

  useEffect(() => {
    loadStudents();
  }, []);

  const createStudent = async () => {
    await fetch(API, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name: newName,
        major: newMajor,
        gpa: Number(newGpa),
      }),
    });

    setNewName("");
    setNewMajor("");
    setNewGpa("");
    loadStudents();
  };

  const deleteStudent = async (id: number) => {
    await fetch(`${API}/${id}`, {
      method: "DELETE",
    });
    loadStudents();
  };

  const startEdit = (student: Student) => {
    setEditingId(student.id);
    setEditName(student.name);
    setEditMajor(student.major);
    setEditGpa(student.gpa.toString());
  };

  const saveEdit = async () => {
    if (editingId === null) return;

    await fetch(`${API}/${editingId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name: editName,
        major: editMajor,
        gpa: Number(editGpa),
      }),
    });

    setEditingId(null);
    loadStudents();
  };

  return (
    <div>
      <h2>Student List</h2>

      <div id="new-student-fields" style={{ marginBottom: "20px" }}>
        <input
          id="new-student-name"
          placeholder="Name"
          value={newName}
          onChange={(e) => setNewName(e.target.value)}
        />
        <input
          id="new-student-major"
          placeholder="Major"
          value={newMajor}
          onChange={(e) => setNewMajor(e.target.value)}
        />
        <input
          id="new-student-gpa"
          placeholder="GPA"
          value={newGpa}
          onChange={(e) => setNewGpa(e.target.value)}
        />
        <button onClick={createStudent}>Create Student</button>
      </div>

      <table id="student-list-table" border={1} cellPadding={8}>
        <thead>
          <tr>
            <th>Name</th>
            <th>Major</th>
            <th>GPA</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {students.map((student) => (
            <tr id={`student-row-${student.id}`} key={student.id}>
              <td id={`student-name-${student.id}`}>{student.name}</td>
              <td id={`student-major-${student.id}`}>{student.major}</td>
              <td id={`student-gpa-${student.id}`}>{student.gpa}</td>
              <td>
                <button id="edit-student-button" onClick={() => startEdit(student)}>
                  Edit
                </button>
                <button id="delete-student-button" onClick={() => deleteStudent(student.id)}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {editingId !== null && (
        <div style={{ marginTop: "20px" }}>
          <h3>Edit Student</h3>
          <input
            id="edit-student-name"
            value={editName}
            onChange={(e) => setEditName(e.target.value)}
          />
          <input
            id="edit-student-major"
            value={editMajor}
            onChange={(e) => setEditMajor(e.target.value)}
          />
          <input
            id="edit-student-gpa"
            value={editGpa}
            onChange={(e) => setEditGpa(e.target.value)}
          />
          <button id="edit-student-save-button" onClick={saveEdit}>
            Save
          </button>
          <button id="edit-student-cancel-button" onClick={() => setEditingId(null)}>
            Cancel
          </button>
        </div>
      )}
    </div>
  );
}

export default StudentList;