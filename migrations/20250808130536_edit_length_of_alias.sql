-- +goose Up
-- +goose StatementBegin
ALTER TABLE [dbo].[CommentSection] ALTER COLUMN [Alias] NVARCHAR(MAX);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
ALTER TABLE [dbo].[CommentSection] ALTER COLUMN [Alias] NVARCHAR(100);
-- +goose StatementEnd
