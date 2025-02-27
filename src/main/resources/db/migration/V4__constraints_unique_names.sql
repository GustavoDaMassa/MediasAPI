ALTER TABLE course ADD CONSTRAINT unique_course_user UNIQUE(name,user_id);

ALTER TABLE projection ADD CONSTRAINT unique_projections_course UNIQUE(name, course_id);

ALTER TABLE assessment ADD CONSTRAINT unique_assessment_projection UNIQUE(identifier, projection_id);