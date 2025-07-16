-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[CommentSection] (
    [Id] UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_CommentSection PRIMARY KEY
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[CommentSection];
-- +goose StatementEnd
