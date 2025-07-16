-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[CommentSection] (
    [Id] UNIQUEIDENTIFIER NOT NULL PRIMARY KEY
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[CommentSection];
-- +goose StatementEnd
