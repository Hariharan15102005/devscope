MERGE INTO projects (id, project_key, name, created_at)
KEY (id)
VALUES (1, 'devscope-demo', 'DevScope Demo Project', CURRENT_TIMESTAMP());

MERGE INTO analysis_runs (id, project_id, run_key, status, created_at)
KEY (id)
VALUES (1, 1, 'devscope-demo-analysis-001', 'COMPLETED', CURRENT_TIMESTAMP());

ALTER TABLE projects ALTER COLUMN id RESTART WITH 2;
ALTER TABLE analysis_runs ALTER COLUMN id RESTART WITH 2;
