export interface InstructorSelectionRound {
  SelectionId: string;
  Information: string;
  BeginTime: string;
  EndTime: string;
  IsActive: boolean;
}

export interface InstructorInfo {
  SelectionId: string;
  TeacherId: string;
  TeacherName: string;
  MajorName?: string;
  Email?: string;
  Telephone?: string;
  SelectedCount: number;
  Capacity: number;
  Status: 'N' | 'P' | 'C';
}

export interface Information {
  SelectionId: string;
  Information: string;
}