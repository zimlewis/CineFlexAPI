-- +goose Up
-- +goose StatementBegin
ALTER TABLE [dbo].[CommentSection] ADD [Alias] NVARCHAR(100);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
ALTER TABLE [dbo].[CommentSection] DROP COLUMN [Alias];
-- +goose StatementEnd
